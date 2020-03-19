package ua.vyshnyak.repository.impl.inmemory;

import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.TestUtils;

class TicketRepositoryImplTest extends AbstractCrudRepositoryTest<Ticket, TicketRepositoryImpl> {

    @Override
    public TicketRepositoryImpl createRepository() {
        return new TicketRepositoryImpl();
    }

    @Override
    public Ticket createEntity(Long id) {
        Ticket ticket = TestUtils.createTicket(1L);
        ticket.setId(id);
        return ticket;
    }
}