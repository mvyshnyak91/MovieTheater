package ua.vyshnyak.services.strategies.impl;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.strategies.DiscountStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class BirthdayStrategy implements DiscountStrategy {
    static final int DAYS = 5;
    static final int DISCOUNT = 5;

    @Override
    public BigDecimal getDiscountPercent(User user, LocalDateTime airDateTime, long numberOfTickets) {
        if (user.getDateOfBirth() != null) {
            LocalDate birthDate = user.getDateOfBirth();
            LocalDate from = airDateTime.minusDays(DAYS).toLocalDate();
            LocalDate to = airDateTime.plusDays(DAYS).toLocalDate();
            if (birthDate.isAfter(from) && birthDate.isBefore(to)) {
                return BigDecimal.valueOf(DISCOUNT);
            }
        }
        return BigDecimal.ZERO;
    }
}
