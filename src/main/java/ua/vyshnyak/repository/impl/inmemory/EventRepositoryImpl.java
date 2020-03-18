package ua.vyshnyak.repository.impl.inmemory;

import org.springframework.stereotype.Component;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.repository.EventRepository;

import java.util.Optional;

@Component
//@Profile("default")
public class EventRepositoryImpl extends AbstractCrudRepository<Event> implements EventRepository {
    @Override
    public Optional<Event> getByName(String name) {
        return entities.values().stream()
                .filter(event -> event.getName().equals(name))
                .findFirst();
    }
}
