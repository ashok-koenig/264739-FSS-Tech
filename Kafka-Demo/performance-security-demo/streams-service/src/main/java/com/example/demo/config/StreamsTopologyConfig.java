package com.example.demo.config;

import com.example.demo.model.Order;
import com.example.demo.model.OrderConfirmation;
import com.example.demo.model.Payment;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class StreamsTopologyConfig {

    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final JsonSerde<Order> ORDER_SERDE = new JsonSerde<>(Order.class);
    private static final JsonSerde<Payment> PAYMENT_SERDE = new JsonSerde<>(Payment.class);
    private static final JsonSerde<OrderConfirmation> CONFIRMATION_SERDE = new JsonSerde<>(OrderConfirmation.class);

    @Bean
    public KStream<String, Order> topology(StreamsBuilder builder) {
        // source processor: reads the "orders" topic into a KStream of <key, Order>
        KStream<String, Order> orders = builder.stream("orders", Consumed.with(STRING_SERDE, ORDER_SERDE));
        // source processor: reads the "payments" topic into a KStream of <key, Payment>
        KStream<String, Payment> payments = builder.stream("payments", Consumed.with(STRING_SERDE, PAYMENT_SERDE));

        // ---- STATELESS: filter + map - decided from the current record only, nothing remembered ----
        KStream<String, Order> validOrders = orders
                // drop orders with no/invalid quantity - each record evaluated independently
                .filter((key, order) -> order.getQuantity() != null && order.getQuantity() > 0)
                // transform value in place - uppercase the item name, key/other records untouched
                .mapValues(order -> {
                    order.setItem(order.getItem().toUpperCase());
                    return order;
                })
                // side-effect only (logging) - passes records through unchanged
                .peek((key, order) -> System.out.println("[STATELESS] order ready: " + order));

        // ---- STATEFUL - AGGREGATION: running count per item, remembered across all orders ----
        validOrders
                // re-key the stream by item so all orders for the same item land in the same partition/group
                .groupBy((key, order) -> order.getItem(), Grouped.with(STRING_SERDE, ORDER_SERDE))
                // count keeps a running total per item in a state store ("order-count-by-item"), never resets
                .count(Materialized.as("order-count-by-item"))
                // convert the KTable of running counts back into a changelog KStream for downstream logging
                .toStream()
                .peek((item, count) -> System.out.println("[STATEFUL-AGGREGATION] " + item + " total so far = " + count));

        // ---- STATEFUL - WINDOWING: same idea, but counts reset every 1-minute window ----
        validOrders
                // re-key by item, same as the aggregation above
                .groupBy((key, order) -> order.getItem(), Grouped.with(STRING_SERDE, ORDER_SERDE))
                // bucket records into fixed 1-minute tumbling windows; late/out-of-order records past the window are dropped (no grace period)
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                // count per (item, window) pair - independent tally that resets each new window
                .count()
                // emit each windowed count update as a stream record keyed by Windowed<item>
                .toStream()
                .peek((window, count) -> System.out.println("[STATEFUL-WINDOWING] " + window.key()
                        + " window=" + window.window().startTime() + " count=" + count));

        // ---- STATEFUL - JOIN: must buffer orders & payments so a match within 5 min still joins ----
        validOrders.join(
                        payments,
                        // join function: fires once a matching order+payment pair is found within the window, builds the confirmation payload
                        (order, payment) -> {
                            OrderConfirmation confirmation = new OrderConfirmation();
                            confirmation.setOrderId(order.getId());
                            confirmation.setItem(order.getItem());
                            confirmation.setOrderAmount(order.getPrice() * order.getQuantity());
                            confirmation.setPaidAmount(payment.getAmount());
                            confirmation.setPaymentStatus(payment.getStatus());
                            confirmation.setConfirmationMessage(
                                    "SUCCESS".equalsIgnoreCase(payment.getStatus()) ? "CONFIRMED" : "PAYMENT_ISSUE");
                            return confirmation;
                        },
                        // both streams must share the same key within 5 minutes of each other to be joined - backed by state stores on both sides
                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)),
                        StreamJoined.with(STRING_SERDE, ORDER_SERDE, PAYMENT_SERDE)
                )
                .peek((key, confirmation) -> System.out.println("[STATEFUL-JOIN] " + confirmation))
                // sink processor: writes the joined confirmations out to the "order-confirmations" topic
                .to("order-confirmations", Produced.with(STRING_SERDE, CONFIRMATION_SERDE));

        // return the original orders stream so Spring registers this bean/topology
        return orders;
    }
}
