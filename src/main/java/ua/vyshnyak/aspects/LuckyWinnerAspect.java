package ua.vyshnyak.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import ua.vyshnyak.entities.Ticket;

import java.math.BigDecimal;
import java.util.Random;

@Aspect
@Component
public class LuckyWinnerAspect {
    @Before("execution(* ua.vyshnyak.services.impl.BookingServiceImpl.bookTicket(..)) && args(ticket)")
    public void applyLuckyWinner(Ticket ticket) {
        if (checkLucky()) {
            ticket.setTicketPrice(BigDecimal.ZERO);
        }
    }

    boolean checkLucky() {
        return new Random().nextBoolean();
    }
}
