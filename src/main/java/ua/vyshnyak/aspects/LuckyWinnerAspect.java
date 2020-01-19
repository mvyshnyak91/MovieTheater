package ua.vyshnyak.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ua.vyshnyak.entities.Ticket;

import java.math.BigDecimal;
import java.util.Random;

@Aspect
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
