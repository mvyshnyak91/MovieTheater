package ua.vyshnyak.services;

import ua.vyshnyak.entities.Event;

public interface EventService extends GenericService<Event> {
    Event getByName(String name);
}
