package ua.vyshnyak.aspects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.TestUtils;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CounterAspectTest {
    private CounterAspect counterAspect;

    @BeforeEach
    void setUp() {
        counterAspect = new CounterAspect();
    }

    @ParameterizedTest
    @MethodSource("countArgs")
    void countGetByName(int callsCount, int expectedCounter) {
        Event event = TestUtils.createEvent("event");
        Stream.iterate(1, i -> i++)
                .limit(callsCount)
                .map(i -> event)
                .forEach(counterAspect::countGetByName);
        assertThat(counterAspect.getEventCounterInfo(event).getGetCounter(), is(expectedCounter));
    }

    @ParameterizedTest
    @MethodSource("countArgs")
    void countGetTicketsPrice(int callsCount, int expectedCounter) {
        Event event = TestUtils.createEvent("event");
        Stream.iterate(1, i -> i++)
                .limit(callsCount)
                .map(i -> event)
                .forEach(counterAspect::countGetTicketsPrice);
        assertThat(counterAspect.getEventCounterInfo(event).getPriceRequestCounter(), is(expectedCounter));
    }

    @ParameterizedTest
    @MethodSource("countArgs")
    void countBookTicket(int callsCount, int expectedCounter) {
        Ticket ticket = TestUtils.createTicket(1L);
        Event event = TestUtils.createEvent("event");
        ticket.setEvent(event);
        Stream.iterate(1, i -> i++)
                .limit(callsCount)
                .map(i ->ticket)
                .forEach(counterAspect::countBookTicket);
        assertThat(counterAspect.getEventCounterInfo(event).getBookTicketCounter(), is(expectedCounter));
    }

    private static Stream<Arguments> countArgs() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(2, 2),
                Arguments.of(3, 3)
        );
    }

}