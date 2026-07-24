package com.example.demo.config;

import com.example.demo.model.Order;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JacksonJsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class StreamsTopologyConfig {

    @Bean
    public KStream<String, Order> topology(StreamsBuilder builder){
        // Reads the "orders" topic into a Kstream
        KStream<String, Order> orders = builder.stream("orders",
                Consumed.with(Serdes.String(), new JacksonJsonSerde<>(Order.class)));

        // stateless transformation: filter + map
        KStream<String, Order> validOrders = orders
                .filter(((key, order) -> order.getQuantity() !=null && order.getQuantity() > 0))
                .mapValues((order)->{
                    order.setItem(order.getItem().toUpperCase());
                    return order;
                }).peek(((key, order) -> System.out.println("[Stateless] order ready: "+ order) ));

        // Stateful - Count the number of orders
        validOrders
                .groupBy((key, order)-> order.getItem(),
                        Grouped.with(Serdes.String(), new JacksonJsonSerde<>(Order.class)))
                .count(Materialized.as("order-count-by-item"))
                .toStream()
                .peek((item, count)-> System.out.println("[Stateful] : total order -> "+ count));

        // Stateful - Count the number of orders in last 5 minutes (Windowing)
        validOrders
                .groupBy((key, order)-> order.getItem(),
                        Grouped.with(Serdes.String(), new JacksonJsonSerde<>(Order.class)))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .peek((item, count)->
                        System.out.println("[Stateful] : last 5 minutes order -> "+ count));

        return orders;
    }
}
