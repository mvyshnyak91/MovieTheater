package ua.vyshnyak.repository.impl;

import org.junit.jupiter.api.Test;
import ua.vyshnyak.entities.BaseEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

abstract class AbstractCrudRepositoryTest<E extends BaseEntity, R extends AbstractCrudRepository<E>> {

    private R abstractCrudRepository = createRepository();

    public abstract R createRepository();
    public abstract E createEntity();

    R getRepository() {
       return abstractCrudRepository;
    }

    @Test
    void persist() {
       E entity = createEntity();
       abstractCrudRepository.persist(entity);
       assertThat(abstractCrudRepository.entities.size(), is(1));
       assertThat(abstractCrudRepository.entities.get(entity.getId()), is(entity));
    }

    @Test
    void update() {
       E entity = createEntity();
       abstractCrudRepository.persist(entity);
       assertThat(abstractCrudRepository.entities.size(), is(1));
       assertThat(abstractCrudRepository.entities.get(entity.getId()), is(entity));
    }

    @Test
    void find() {
       E entity = createEntity();
       abstractCrudRepository.entities.put(1L, entity);
       Optional<E> entity1 = abstractCrudRepository.find(1L);
       assertThat(entity1.isPresent(), is(true));
       assertThat(entity1.get(), is(entity));
    }

    @Test
    void findAll() {
    }

    @Test
    void delete() {
    }
}