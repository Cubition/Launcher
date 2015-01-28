package net.cubition.launcher;

/**
 * A Task is a event that is to occur.
 */
public class Task<T, X> {
    private final T key;
    private final X value;

    public Task(T key, X value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public X getValue() {
        return value;
    }
}
