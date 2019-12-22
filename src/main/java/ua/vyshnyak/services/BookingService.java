package ua.vyshnyak.services;

import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public interface BookingService {
    BigDecimal getTicketsPrice(Event event, LocalDateTime dateTime, User user, Set<Long> seats);
    void bookTickets(Set<Ticket> tickets);
    void bookTicket(Ticket ticket);
    Set<Long> getAvailableSeats(Event event, LocalDateTime airDate);
    Set<Ticket> getPurchasedTicketsForEvent(Event event,  LocalDateTime dateTime);
}
