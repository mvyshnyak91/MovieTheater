package ua.vyshnyak.services.impl;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.DiscountService;
import ua.vyshnyak.services.strategies.DiscountStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private List<DiscountStrategy> discountStrategies;

    public DiscountServiceImpl(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }

    @Override
    public BigDecimal getDiscountPercent(User user, LocalDateTime airDateTime, long numberOfTickets) {
        BigDecimal discount = BigDecimal.ZERO;
        for (DiscountStrategy discountStrategy : discountStrategies) {
            BigDecimal currentDiscount = discountStrategy.getDiscountPercent(user, airDateTime, numberOfTickets);
            if (currentDiscount.compareTo(discount) > 0) {
                discount = currentDiscount;
            }
        }
        return discount;
    }
}
