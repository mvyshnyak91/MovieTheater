package ua.vyshnyak.repository;

import ua.vyshnyak.entities.BaseEntity;

import java.util.Collection;
import java.util.Optional;

public interface GenericCrudRepository<E extends BaseEntity> {
    void persist(E entity);
    void update(E entity);
    Optional<E> find(Long id);
    Collection<E> findAll();
    void delete(E entity);
}
