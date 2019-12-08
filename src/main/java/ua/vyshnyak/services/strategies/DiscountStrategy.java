package ua.vyshnyak.services.strategies;

import ua.vyshnyak.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DiscountStrategy {
    BigDecimal getDiscountPercent(User user, LocalDateTime dateTime, long numberOfTickets);
}
