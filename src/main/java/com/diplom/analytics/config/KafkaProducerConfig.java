package com.diplom.analytics.config;

import com.diplom.analytics.tool.model.TrackerEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, TrackerEvent> messageProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        JsonSerializer<TrackerEvent> serializer = new JsonSerializer<>();
        serializer.setAddTypeInfo(true);

        return new DefaultKafkaProducerFactory<>(
                config,
                new StringSerializer(),
                serializer
        );
    }

    @Bean
    public KafkaTemplate<String, TrackerEvent> kafkaTemplate() {
        KafkaTemplate<String, TrackerEvent> template = new KafkaTemplate<>(messageProducerFactory());
        template.setDefaultTopic("tracker-events");
        return template;
    }
}
