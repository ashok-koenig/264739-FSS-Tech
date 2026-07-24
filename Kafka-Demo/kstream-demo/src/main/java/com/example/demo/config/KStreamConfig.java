package com.example.demo.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class KStreamConfig {

    @Bean
    public KStream<String, String> kstreamDemo(StreamsBuilder builder){
        // Read from input-topic
        KStream<String, String> stream = builder.stream(
                "input-topic",
                Consumed.with(Serdes.String(), Serdes.String()));
        // Transform values to uppercase
        stream.mapValues( value -> value.toUpperCase())
                .to("output-topic", Produced.with(Serdes.String(), Serdes.String()));
        return stream;
    }
}
