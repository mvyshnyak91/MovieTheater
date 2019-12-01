package ua.vyshnyak.entities;

public abstract class BaseEntity<K> {
    private K id;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }
}
