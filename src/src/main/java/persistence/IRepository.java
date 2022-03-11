package persistence;

import java.util.List;
import java.util.function.Predicate;

/**
 * Abstract interface that defines how values should be managed.
 * @param <K> the key of how values are identified.
 * @param <V> the values to be persisted/managed.
 */
interface IRepository<K, V> {
    public V create(V value);

    public V readById(K key);

    public List<V> readAll();

    public List<V> readFiltered(Predicate<V> p);

    public V update(V newValue);

    public boolean delete(K key);

}
