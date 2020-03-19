package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vyshnyak.TestUtils;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.exceptions.EntityAlreadyExistsException;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.repository.EventRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @InjectMocks
    private EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;


    @Test
    void getByName() {
        Event event = TestUtils.createEvent("event");
        when(eventRepository.getByName(event.getName())).thenReturn(Optional.of(event));

        Event persistedEvent = eventService.getByName(event.getName());

        assertThat(event, is(persistedEvent));
        verify(eventRepository).getByName(event.getName());
    }

    @Test
    void getByNameEventNotFound() {
        when(eventRepository.getByName("event")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> eventService.getByName("event"));
    }

    @Test
    void save() {
        Event event = TestUtils.createEvent("event");
        eventService.save(event);
        verify(eventRepository).persist(event);
    }

    @Test
    void saveEventAlreadyExists() {
        Event event = TestUtils.createEvent("event");
        when(eventRepository.getByName(event.getName())).thenReturn(Optional.of(event));

        assertThrows(EntityAlreadyExistsException.class, () -> eventService.save(event));
        verify(eventRepository, never()).persist(event);
    }

    @Test
    void saveWithNegativeBasePrice() {
        Event event = TestUtils.createEvent("event");
        event.setBasePrice(new BigDecimal("-1"));

        assertThrows(IllegalStateException.class, () -> eventService.save(event));
        verify(eventRepository, never()).persist(event);
    }

    @Test
    void remove() {
        Event event = TestUtils.createEvent("event");
        eventService.remove(event);
        verify(eventRepository).delete(event);
    }

    @Test
    void getById() {
        Event event = TestUtils.createEvent("event");
        event.setId(1L);
        when(eventRepository.find(event.getId())).thenReturn(Optional.ofNullable(event));

        Event persistedEvent = eventService.getById(event.getId());

        assertThat(event, is(persistedEvent));
        verify(eventRepository).find(event.getId());
    }

    @Test
    void getByIdEventNotFound() {
        when(eventRepository.find(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> eventService.getById(1L));
    }

    @Test
    void getAll() {
        List<Event> events = Arrays.asList(
                TestUtils.createEvent("first event"),
                TestUtils.createEvent("second event")
        );
        when(eventRepository.findAll()).thenReturn(events);

        Collection<Event> allPersistedEvents = eventService.getAll();

        assertThat(events, is(allPersistedEvents));
        verify(eventRepository).findAll();
    }
}