package ua.vyshnyak.repository.impl;

import ua.vyshnyak.entities.Event;
import ua.vyshnyak.repository.EventRepository;

import java.util.Optional;

public class EventRepositoryImpl extends AbstractCrudRepository<Event> implements EventRepository {
    @Override
    public Optional<Event> getByName(String name) {
        return entities.values().stream()
                .filter(event -> event.getName().equals(name))
                .findFirst();
    }
}
