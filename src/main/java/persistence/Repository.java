package persistence;

import persistence.interfaces.IRepository;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class Repository<K, V> implements IRepository<K, V> {
    private HashMap<K, V> map;

    @Override
    public V create(V value) {
        return null;
    }

    @Override
    public V readById(K key) {
        return null;
    }

    @Override
    public List<V> readAll() {
        return null;
    }

    @Override
    public List<V> readFiltered(Predicate<V> p) {
        return null;
    }

    @Override
    public V update(V newValue) {
        return null;
    }

    @Override
    public boolean delete(K key) {
        return false;
    }
}
