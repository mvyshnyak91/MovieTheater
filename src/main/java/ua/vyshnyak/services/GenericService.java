package ua.vyshnyak.services;

import ua.vyshnyak.entities.BaseEntity;
import ua.vyshnyak.exceptions.EntityNotFoundException;

import java.util.Collection;

public interface GenericService<K, E extends BaseEntity> {
    void save(E entity);
    void remove(E entity);
    E getById(K id) throws EntityNotFoundException;
    Collection<E> getAll();
}
