package ua.vyshnyak.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ua.vyshnyak.entities.User;

import java.util.HashMap;
import java.util.Map;

@Aspect
public class DiscountAspect {
    private Map<User, Integer> userIntegerMap = new HashMap<>();

    @Before("execution(* ua.vyshnyak.services.impl.DiscountServiceImpl.getDiscountPercent())")
    public void count() {

    }
}
