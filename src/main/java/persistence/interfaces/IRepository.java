package persistence.interfaces;

import java.util.List;
import java.util.function.Predicate;

/**
 * Abstract interface that defines how values should be managed.
 * @param <K> the key of how values are identified.
 * @param <V> the values to be persisted/managed.
 */
public interface IRepository<K, V> {
    V create(V value);

    V readById(K key);

    List<V> readAll();

    List<V> readFiltered(Predicate<V> p);

    V update(V newValue);

    boolean delete(K key);

}
