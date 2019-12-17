package ua.vyshnyak.services;

import ua.vyshnyak.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DiscountService {
    BigDecimal getDiscountPercent(User user, LocalDateTime airDateTime, long numberOfTickets);
}
