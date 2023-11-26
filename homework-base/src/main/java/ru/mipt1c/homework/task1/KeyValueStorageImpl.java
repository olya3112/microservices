package ru.mipt1c.homework.task1;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;

import java.io.*;
@Component("data")
@Scope("prototype")
public class KeyValueStorageImpl<K, V> implements KeyValueStorage<K, V> {
    private HashMap<K, V> cache = new HashMap<>();
    private StateData stateData = StateData.Open;
    private File file;

    private final String path;



    public KeyValueStorageImpl(String path) throws MalformedDataException {
        this.path = path;
        this.file = new File(path + "/file.dat");
        try (ObjectInputStream objectInPutStream = new ObjectInputStream(Files.newInputStream(file.toPath()));) {
            this.cache = (HashMap<K, V>) objectInPutStream.readObject();

        } catch (IOException | ClassNotFoundException | MalformedDataException exception) {

            if (exception instanceof MalformedDataException) {
                throw new MalformedDataException("The file was changed");
            }

        }
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
    public void close() {
        flush();
        checkStateData();
    }

    @Override
    public void flush() {
        checkStateData();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    Files.newOutputStream(file.toPath()))) {
                objectOutputStream.writeObject(this.cache);
                objectOutputStream.flush();
        } catch (IOException exception) {
                exception.printStackTrace();
        }

    }


    private void checkStateData() {
        if (stateData != StateData.Open)
            throw new IllegalStateException("Data is closed");
    }
}