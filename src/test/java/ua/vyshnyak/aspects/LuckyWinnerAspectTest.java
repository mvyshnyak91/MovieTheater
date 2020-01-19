package ua.vyshnyak.aspects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.Spy;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.services.impl.TestUtils;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LuckyWinnerAspectTest {
    private LuckyWinnerAspect luckyWinnerAspect;

    @BeforeEach
    void setUp() {
        luckyWinnerAspect = Mockito.spy(LuckyWinnerAspect.class);
    }

    @ParameterizedTest
    @MethodSource("applyLuckyArgs")
    void applyLucky(boolean isLucky, BigDecimal ticketPrice, BigDecimal expectedTicketPrice) {
        Ticket ticket = TestUtils.createTicket(1L);
        ticket.setTicketPrice(ticketPrice);
        Mockito.doReturn(isLucky).when(luckyWinnerAspect).checkLucky();
        luckyWinnerAspect.applyLuckyWinner(ticket);
        assertThat(ticket.getTicketPrice(), is(expectedTicketPrice));
    }

    private static Stream<Arguments> applyLuckyArgs() {
        return Stream.of(
                Arguments.of(true, new BigDecimal("20"), BigDecimal.ZERO),
                Arguments.of(false, new BigDecimal("20"), new BigDecimal("20"))
        );
    }

}