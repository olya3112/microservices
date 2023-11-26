package ru.mipt1c.homework.task1;

import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component("kafkaStorage")
public class SerializationKeyValueStorage<K, V> implements KeyValueStorage<K, V> {
    private KafkaTemplate<K, V> kafkaTemplate;
    private StateData stateData = StateData.Open;
    private Map<K, V> cache = new ConcurrentHashMap<>();

    public final String topic;

    @Autowired
    public SerializationKeyValueStorage(Consumer<K, V> consumer, KafkaTemplate<K, V> kafkaTemplate, String topic) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        final int giveUp = 100;
        int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<K, V> consumerRecords = consumer.poll(100L);
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) {
                    break;
                } else {
                    continue;
                }
            }

            consumerRecords.forEach(record -> {
                this.cache.put(record.key(), record.value());
            });

            consumer.commitAsync();
        }
        consumer.close();
    }

    @Override
    public V read(K key) {
        checkStateData();
        return this.cache.get(key);
    }

    @Override
    public boolean exists(K key) {
        checkStateData();
        return this.cache.containsKey(key);
    }

    @Override
    public void write(K key, V value) {
        checkStateData();
        this.cache.put(key, value);

    }

    @Override
    public void delete(K key) {
        checkStateData();
        this.cache.remove(key);
    }

    @Override
    public Iterator<K> readKeys() {
        checkStateData();
        return this.cache.keySet().iterator();
    }

    @Override
    public int size() {
        checkStateData();
        return this.cache.size();
    }

    @Override
    public void flush() {
        checkStateData();
        for (Map.Entry<K, V> entry : this.cache.entrySet()) {
                this.kafkaTemplate.send(topic, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void close() throws IOException {
        flush();
        stateData = StateData.Close;
    }

    private void checkStateData() {
        if (stateData != StateData.Open)
            throw new IllegalStateException("Data is closed");
    }

}
