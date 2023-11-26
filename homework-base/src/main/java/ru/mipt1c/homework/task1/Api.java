package ru.mipt1c.homework.task1;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import ru.mipt1c.homework.task1.Kafka.KafkaConsumer;

import java.io.IOException;

public class Api {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new
                AnnotationConfigApplicationContext(KafkaConsumer.class);
        Consumer<String, String> consumer = (Consumer<String, String>) context.getBean("consumer");
        KafkaTemplate<String, String> kafkaTemplate = (KafkaTemplate<String, String>) context.getBean("kafkaTemplate");
        String topic = (String) context.getBean("topic");
        try {
            SerializationKeyValueStorage<String, String> kafka =
                    (SerializationKeyValueStorage<String, String>) context.getBean("kafkaStorage",
                            consumer, kafkaTemplate, topic);

            kafka.readKeys().forEachRemaining(System.out::println);
            kafka.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        context.close();
    }
}
