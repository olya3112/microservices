package ru.mipt1c.homework.tests.task1;

import org.springframework.context.annotation.*;
import ru.mipt1c.homework.task1.KeyValueStorageImpl;

@Configuration
@ComponentScan("ru.mipt1c.homework.tests.task1")
public class SpringTestConfiguration {
    @Bean("KeyValueStorageStrStr")
    @Scope("prototype")
    public KeyValueStorageImpl<String, String> keyValueStorageStrStr(String path) {
        return new KeyValueStorageImpl<>(path);
    }

    @Bean("KeyValueStorageIntDouble")
    @Scope("prototype")
    public KeyValueStorageImpl<Integer, Double> keyValueStorageIntDouble(String path) {
        return new KeyValueStorageImpl<>(path);
    }

    @Bean("KeyValueStorageStKeySt")
    @Scope("prototype")
    public KeyValueStorageImpl<StudentKey, Student> keyValueStorageStKeySt(String path) {
        return new KeyValueStorageImpl<>(path);
    }
}

