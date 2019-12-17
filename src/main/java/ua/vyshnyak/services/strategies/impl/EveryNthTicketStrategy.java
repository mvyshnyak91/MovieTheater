package ua.vyshnyak.services.strategies.impl;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.strategies.DiscountStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EveryNthTicketStrategy implements DiscountStrategy {
    static final int BASE_DISCOUNT = 50;
    @Override
    public BigDecimal getDiscountPercent(User user, LocalDateTime dateTime, long numberOfTickets) {
        Long number = user.getTickets().size() + numberOfTickets;
        if (number % 10 == 0) {
            return BigDecimal.valueOf(BASE_DISCOUNT);
        }
        return BigDecimal.ZERO;
    }
}
