package ua.vyshnyak.services.strategies.impl;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.strategies.DiscountStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class EveryNthTicketStrategy implements DiscountStrategy {
    static final int BASE_DISCOUNT = 50;
    private static final int DISCOUNT_TICKET_COUNT = 10;
    @Override
    public BigDecimal getDiscountPercent(User user, LocalDateTime airDateTime, long numberOfTickets) {
        Long totalNumberOfTickets = user.getTickets().size() + numberOfTickets;
        if (totalNumberOfTickets % DISCOUNT_TICKET_COUNT == 0) {
            return BigDecimal.valueOf(BASE_DISCOUNT);
        }
        return BigDecimal.ZERO;
    }
}
