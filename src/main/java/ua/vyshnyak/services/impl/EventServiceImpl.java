package ua.vyshnyak.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.repository.EventRepository;
import ua.vyshnyak.services.EventService;

import java.util.Collection;

public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event getByName(String name) {
        return eventRepository.getByName(name).orElseThrow(() -> new EntityNotFoundException(""));
    }

    @Override
    public void save(Event event) {
        eventRepository.persist(event);
    }

    @Override
    public void remove(Event event) {
        eventRepository.delete(event);
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.find(id).orElseThrow(() -> new EntityNotFoundException(""));
    }

    @Override
    public Collection<Event> getAll() {
        return eventRepository.findAll();
    }
}
