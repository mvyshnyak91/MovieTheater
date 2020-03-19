package ua.vyshnyak.repository.impl.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.vyshnyak.entities.BaseEntity;
import ua.vyshnyak.repository.impl.inmemory.AbstractCrudRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractCrudRepositoryTest<E extends BaseEntity, R extends AbstractCrudRepository<E>> {

    private R abstractCrudRepository;

    public abstract R createRepository();
    public abstract E createEntity(Long id);

    R getRepository() {
       return abstractCrudRepository;
    }

    @BeforeEach
    void setUp() {
        abstractCrudRepository = createRepository();
    }

    @ParameterizedTest
    @MethodSource("getPersistArgs")
    void persist(Long id) {
       E entity = createEntity(id);
       abstractCrudRepository.persist(entity);
       assertThat(entity.getId(), is(1L));
       assertThat(abstractCrudRepository.entities.size(), is(1));
       assertThat(abstractCrudRepository.entities.get(entity.getId()), is(entity));
    }

    private static Stream<Arguments> getPersistArgs() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of(1L)
        );
    }

    @Test
    void update() {
       E entity = createEntity(1L);
       abstractCrudRepository.entities.put(entity.getId(), entity);
       abstractCrudRepository.update(entity);
       assertThat(abstractCrudRepository.entities.size(), is(1));
       assertThat(abstractCrudRepository.entities.get(entity.getId()), is(entity));
    }

    @Test
    void updateNotPersistedEntity() {
        E entity = createEntity(null);
        assertThrows(IllegalStateException.class,() -> abstractCrudRepository.update(entity));
        assertThat(abstractCrudRepository.entities.isEmpty(), is(true));
    }

    @Test
    void find() {
       E entity = createEntity(1L);
       abstractCrudRepository.entities.put(entity.getId(), entity);
       Optional<E> persistedEntity = abstractCrudRepository.find(1L);
       assertThat(persistedEntity.isPresent(), is(true));
       assertThat(persistedEntity.get(), is(entity));
    }

    @Test
    void findAll() {
        List<E> entities = Arrays.asList(
                createEntity(1L),
                createEntity(2L)
        );
        entities.forEach(entity -> abstractCrudRepository.entities.put(entity.getId(), entity));
        Collection<E> eCollection = abstractCrudRepository.findAll();
        assertThat(eCollection.containsAll(entities), is(true));
    }

    @Test
    void delete() {
        E entity = createEntity(1L);
        abstractCrudRepository.entities.put(entity.getId(), entity);
        abstractCrudRepository.delete(entity);
        assertThat(abstractCrudRepository.entities.isEmpty(), is(true));
    }
}