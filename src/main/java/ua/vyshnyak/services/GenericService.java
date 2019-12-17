package ua.vyshnyak.services;

import ua.vyshnyak.entities.BaseEntity;

import java.util.Collection;

public interface GenericService<E extends BaseEntity> {
    void save(E entity);
    void remove(E entity);
    E getById(Long id);
    Collection<E> getAll();
}
