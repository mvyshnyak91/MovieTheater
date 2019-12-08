package ua.vyshnyak.repository.impl;

import ua.vyshnyak.entities.BaseEntity;
import ua.vyshnyak.repository.GenericCrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AbstractCrudRepository<E extends BaseEntity> implements GenericCrudRepository<E> {
    protected Map<Long, E> entities = new HashMap<>();
    private long idCounter;

    @Override
    public void persist(E entity) {
        if (entity.getId() != null) {
            entity.setId(++idCounter);
        }
        entities.put(entity.getId(), entity);
    }

    @Override
    public void update(E entity) {
        if (entity.getId() != null) {
            entities.put(entity.getId(), entity);
        }
    }

    @Override
    public Optional<E> find(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Collection<E> findAll() {
        return entities.values();
    }

    @Override
    public void delete(E entity) {
        entities.remove(entity.getId());
    }
}
