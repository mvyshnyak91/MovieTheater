package ua.vyshnyak.repository.impl;

import org.junit.jupiter.api.Test;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.services.impl.TestUtils;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class EventRepositoryImplTest extends AbstractCrudRepositoryTest<Event, EventRepositoryImpl> {

    @Test
    void getByName() {
        Event event = createEntity();
        abstractCrudRepository.entities.put(1L, event);
        Optional<Event> event1 = abstractCrudRepository.getByName("Event");
        assertThat(event1.isPresent(), is(true));
        assertThat(event1.get(), is(event));
    }

    @Override
    public EventRepositoryImpl createRepository() {
        return new EventRepositoryImpl();
    }

    @Override
    public Event createEntity() {
        return TestUtils.createEvent("Event");
    }
}