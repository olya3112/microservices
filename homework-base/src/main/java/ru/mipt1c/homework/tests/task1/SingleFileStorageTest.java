package ru.mipt1c.homework.tests.task1;

import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.mipt1c.homework.task1.KeyValueStorage;
import ru.mipt1c.homework.task1.MalformedDataException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})
@SuppressWarnings("unchecked")
public class SingleFileStorageTest extends AbstractSingleFileStorageTest implements ApplicationContextAware {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected KeyValueStorage<String, String> buildStringsStorage(String path) throws MalformedDataException {
        KeyValueStorage<String, String> keyValue = (KeyValueStorage<String, String>)
                applicationContext.getBean("KeyValueStorageStrStr", path);
        return keyValue;
    }

    @Override
    protected KeyValueStorage<Integer, Double> buildNumbersStorage(String path) throws MalformedDataException {
        KeyValueStorage<Integer, Double> keyValue = (KeyValueStorage<Integer, Double>)
                applicationContext.getBean("KeyValueStorageIntDouble", path);
        return keyValue;
    }

    @Override
    protected KeyValueStorage<StudentKey, Student> buildPojoStorage(String path) throws MalformedDataException {
        KeyValueStorage<StudentKey, Student> keyValue= (KeyValueStorage<StudentKey, Student>)
                applicationContext.getBean("KeyValueStorageStKeySt", path);
        return keyValue;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

