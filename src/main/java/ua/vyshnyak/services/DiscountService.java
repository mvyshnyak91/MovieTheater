package ua.vyshnyak.services;

import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.User;

import java.time.LocalDateTime;

public interface DiscountService {
    byte getDiscount(User user, Event event, LocalDateTime airDateTime, long numberOfTickets);
}
