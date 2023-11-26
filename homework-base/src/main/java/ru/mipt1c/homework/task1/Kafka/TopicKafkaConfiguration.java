package ru.mipt1c.homework.task1.Kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class TopicKafkaConfiguration {
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(AdminClientConfig.CLIENT_ID_CONFIG, "storage-consumer");
        return new KafkaAdmin(properties);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("storage-topic-main")
                .partitions(1)
                .replicas(1)
                .build();
    }


}
