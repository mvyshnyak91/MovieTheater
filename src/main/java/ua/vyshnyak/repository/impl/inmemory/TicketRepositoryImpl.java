package ua.vyshnyak.repository.impl.inmemory;

import org.springframework.stereotype.Component;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.repository.TicketRepository;

@Component
public class TicketRepositoryImpl extends AbstractCrudRepository<Ticket> implements TicketRepository {
}
