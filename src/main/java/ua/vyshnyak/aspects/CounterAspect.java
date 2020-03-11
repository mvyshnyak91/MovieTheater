package ua.vyshnyak.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.Ticket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Aspect
@Component
public class CounterAspect {
    private Map<Event, EventCounterInfo> eventCounterInfoMap = new HashMap<>();

    @AfterReturning(value = "execution(* ua.vyshnyak.services.impl.EventServiceImpl.getByName(..))", returning = "event")
    public void countGetByName(Event event) {
        applyAspect(event, EventCounterInfo::incrementGetCounter);
    }

    @Before("execution(* ua.vyshnyak.services.impl.BookingServiceImpl.getTicketsPrice(..)) && args(event, ..)")
    public void countGetTicketsPrice(Event event) {
        applyAspect(event, EventCounterInfo::incrementPriceRequestCounter);
    }

    @Before("execution(* ua.vyshnyak.services.impl.BookingServiceImpl.bookTicket(..)) && args(ticket)")
    public void countBookTicket(Ticket ticket) {
        applyAspect(ticket.getEvent(), EventCounterInfo::incrementBookTicketCounter);
    }

    private void applyAspect(Event event, Consumer<EventCounterInfo> consumer) {
        if (eventCounterInfoMap.containsKey(event)) {
            consumer.accept(eventCounterInfoMap.get(event));
        } else {
            EventCounterInfo eventCounterInfo = new EventCounterInfo();
            consumer.accept(eventCounterInfo);
            eventCounterInfoMap.put(event, eventCounterInfo);
        }
    }

    public EventCounterInfo getEventCounterInfo(Event event) {
        return eventCounterInfoMap.get(event);
    }
}
