package com.example.demo.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class KTableConfig {

    @Bean
    public KTable<String, String> ktableDemo(StreamsBuilder builder){
        // Read from input-topic
        KTable<String, String> table = builder.table(
                "input-topic",
                Consumed.with(Serdes.String(), Serdes.String()));

        // Simply forward to another topic
        table.toStream().to("output-topic", Produced.with(Serdes.String(), Serdes.String()));
        return table;
    }
}
