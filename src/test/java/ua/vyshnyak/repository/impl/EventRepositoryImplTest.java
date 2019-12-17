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
        Event event = createEntity(1L);
        getRepository().entities.put(event.getId(), event);
        Optional<Event> event1 = getRepository().getByName("Event");
        assertThat(event1.isPresent(), is(true));
        assertThat(event1.get(), is(event));
    }

    @Override
    public EventRepositoryImpl createRepository() {
        return new EventRepositoryImpl();
    }

    @Override
    public Event createEntity(Long id) {
        Event event = TestUtils.createEvent("Event");
        event.setId(id);
        return event;
    }
}