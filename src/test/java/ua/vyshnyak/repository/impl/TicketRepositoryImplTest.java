package ua.vyshnyak.repository.impl;

import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.services.impl.TestUtils;

class TicketRepositoryImplTest extends AbstractCrudRepositoryTest<Ticket, TicketRepositoryImpl> {

    @Override
    public TicketRepositoryImpl createRepository() {
        return new TicketRepositoryImpl();
    }

    @Override
    public Ticket createEntity() {
        return TestUtils.createTicket(1L);
    }
}