package ua.vyshnyak.aspects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.BookingService;
import ua.vyshnyak.services.EventService;
import ua.vyshnyak.services.impl.TestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "classpath:beans.xml", "classpath:discount-beans.xml" })
class CounterAspectTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CounterAspect counterAspect;

    private Event event;

    @BeforeEach
    void setUp() {
        event = TestUtils.createEvent("event");
        eventService.save(event);
    }

    @AfterEach
    void cleanUp() {
        eventService.remove(event);
    }

    @Test
    void countGetByName() {
        Event event = eventService.getByName("event");
        EventCounterInfo counterInfo = counterAspect.getEventCounterInfo(event);
        assertThat(counterInfo.getGetCounter(), is(1));
    }

    @Test
    void countGetTicketsPrice() {
        Set<Long> seats = new HashSet<>(Arrays.asList(1L, 2L));
        bookingService.getTicketsPrice(event, TestUtils.airDateTime, new User(), seats);
        EventCounterInfo counterInfo = counterAspect.getEventCounterInfo(event);
        assertThat(counterInfo.getPriceRequestCounter(), is(1));
    }

    @Test
    void countBookTicket() {
        Ticket ticket = TestUtils.createTicket(new User(), event, 1L);
        bookingService.bookTicket(ticket);
        EventCounterInfo counterInfo = counterAspect.getEventCounterInfo(event);
        assertThat(counterInfo.getBookTicketCounter(), is(1));
    }
}