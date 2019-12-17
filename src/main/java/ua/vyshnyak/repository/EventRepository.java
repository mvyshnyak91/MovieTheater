package ua.vyshnyak.repository;

import ua.vyshnyak.entities.Event;

import java.util.Optional;

public interface EventRepository extends GenericCrudRepository<Event> {
    Optional<Event> getByName(String name);
}
