package ua.vyshnyak.services.strategies.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.entities.User;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ua.vyshnyak.TestUtils.*;
import static ua.vyshnyak.TestUtils.createTicket;
import static ua.vyshnyak.TestUtils.createTickets;
import static ua.vyshnyak.TestUtils.createUser;
import static ua.vyshnyak.services.strategies.impl.EveryNthTicketStrategy.BASE_DISCOUNT;

class EveryNthTicketStrategyTest {
    private EveryNthTicketStrategy everyNthTicketStrategy;

    @BeforeEach
    void setUp() {
        everyNthTicketStrategy = new EveryNthTicketStrategy();
    }

    @ParameterizedTest
    @MethodSource("getDiscountPercentArgs")
    void getDiscountPercent(NavigableSet<Ticket> tickets, long numberOfTickets, BigDecimal expectedDiscountPercent) {
        User user = createUser();
        user.setTickets(tickets);

        BigDecimal discountPercent = everyNthTicketStrategy.getDiscountPercent(user, airDateTime, numberOfTickets);

        assertThat(discountPercent, is(expectedDiscountPercent));
    }

    private static Stream<Arguments> getDiscountPercentArgs() {
        return Stream.of(
                Arguments.of(createTickets(createTicket(1L), createTicket(2L)), 8L, BigDecimal.valueOf(BASE_DISCOUNT)),
                Arguments.of(createTickets(createTicket(1L), createTicket(2L)), 18L, BigDecimal.valueOf(BASE_DISCOUNT)),
                Arguments.of(createTickets(createTicket(1L), createTicket(2L)), 17L, BigDecimal.ZERO),
                Arguments.of(Collections.emptyNavigableSet(), 8L, BigDecimal.ZERO),
                Arguments.of(Collections.emptyNavigableSet(), 9L, BigDecimal.ZERO),
                Arguments.of(Collections.emptyNavigableSet(), 10L, BigDecimal.valueOf(BASE_DISCOUNT))
        );
    }

}