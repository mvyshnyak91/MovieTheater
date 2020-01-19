package ua.vyshnyak.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import ua.vyshnyak.entities.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Aspect
public class DiscountAspect {
    private Map<User, Integer> userIntegerMap = new HashMap<>();

    @AfterReturning(value = "execution(* ua.vyshnyak.services.strategies.*.getDiscountPercent(..)) && args(user, ..)",
            returning = "discountPercent")
    public void countGetDiscountPercent(User user, BigDecimal discountPercent) {
        if (discountPercent.compareTo(BigDecimal.ZERO) > 0) {
            if (userIntegerMap.containsKey(user)) {
                userIntegerMap.put(user, userIntegerMap.get(user) + 1);
            } else {
                userIntegerMap.put(user, 1);
            }
        }
    }

    public Integer getCount(User user) {
        return userIntegerMap.get(user);
    }
}
