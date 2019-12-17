package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.repository.EventRepository;
import ua.vyshnyak.services.EventService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @InjectMocks
    private EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;


    @Test
    void getByName() {
        Event event = TestUtils.createEvent("event");
        Mockito.when(eventRepository.getByName(event.getName())).thenReturn(Optional.of(event));
        Event event1 = eventService.getByName(event.getName());
        assertThat(event, is(event1));
        Mockito.verify(eventRepository).getByName(event.getName());
    }

    @Test
    void getByNameEventNotFound() {
        Mockito.when(eventRepository.getByName("event")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> eventService.getByName("event"));
    }

    @Test
    void save() {
        Event event = TestUtils.createEvent("event");
        eventService.save(event);
        Mockito.verify(eventRepository).persist(event);
    }

    @Test
    void remove() {
        Event event = TestUtils.createEvent("event");
        eventService.remove(event);
        Mockito.verify(eventRepository).delete(event);
    }

    @Test
    void getById() {
        Event event = TestUtils.createEvent("event");
        event.setId(1L);
        Mockito.when(eventRepository.find(event.getId())).thenReturn(Optional.ofNullable(event));
        Event event1 = eventService.getById(event.getId());
        assertThat(event, is(event1));
        Mockito.verify(eventRepository).find(event.getId());
    }

    @Test
    void getByIdEventNotFound() {
        Mockito.when(eventRepository.find(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> eventService.getById(1L));
    }

    @Test
    void getAll() {
        List<Event> events = Arrays.asList(
                TestUtils.createEvent("first event"),
                TestUtils.createEvent("second event")
        );

        Mockito.when(eventRepository.findAll()).thenReturn(events);
        Collection<Event> events1 = eventService.getAll();
        assertThat(events, is(events1));
        Mockito.verify(eventRepository).findAll();
    }
}