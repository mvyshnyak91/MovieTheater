package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import ua.vyshnyak.entities.*;
import ua.vyshnyak.repository.TicketRepository;
import ua.vyshnyak.repository.UserRepository;
import ua.vyshnyak.services.DiscountService;
import ua.vyshnyak.services.UserService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vyshnyak.entities.EventRating.HIGH;
import static ua.vyshnyak.entities.EventRating.LOW;
import static ua.vyshnyak.entities.EventRating.MID;
import static ua.vyshnyak.services.impl.TestUtils.*;
import static ua.vyshnyak.services.impl.TestUtils.dateTime;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private DiscountService discountService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;

    private Event event;
    private User user;
    private NavigableSet<Ticket> tickets;

    @BeforeEach
    void setUp() {
        event = createEvent(createAuditorium("Auditorium", 10, createSeats(8L, 9L, 10L)));
        user = createUser();
        tickets = createTickets(
                createTicket(createEvent("event1"), dateTime, 1L),
                createTicket(createEvent("event1"), dateTime, 2L),
                createTicket(createEvent("event1"), dateTime.plusDays(1), 1L),
                createTicket(createEvent("event2"), dateTime, 1L)
        );
    }

    @ParameterizedTest
    @MethodSource("getTicketsPriceArgs")
    void getTicketsPrice(EventRating eventRating, Set<Long> seats, BigDecimal discountPercent,
                         BigDecimal expectedPrice) {
        event.setRating(eventRating);
        when(discountService.getDiscountPercent(eq(user), eq(dateTime), anyLong())).thenReturn(discountPercent);
        BigDecimal ticketsPrice = bookingService.getTicketsPrice(event, dateTime, user, seats);
        assertThat(ticketsPrice, is(expectedPrice));
    }

    private static Stream<Arguments> getTicketsPriceArgs() {
        return Stream.of(
                Arguments.of(LOW, createSeats(9L), BigDecimal.ZERO, new BigDecimal("20.00")),
                Arguments.of(HIGH, createSeats(1L), BigDecimal.ZERO, new BigDecimal("12.00")),
                Arguments.of(MID, createSeats(10L), BigDecimal.valueOf(5), new BigDecimal("19.00")),
                Arguments.of(MID, createSeats(7L), BigDecimal.ZERO, new BigDecimal("10.00"))
        );
    }

    @ParameterizedTest
    @MethodSource("getBookTicketsArgs")
    void bookTickets(User user, User user1, long numberOfTickets, VerificationMode userUpdates, VerificationMode ticketsSaves) {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.ofNullable(user1));
        bookingService.bookTickets(createTickets(user, event, numberOfTickets));
        verify(userService, userUpdates).save(any());
        verify(ticketRepository, ticketsSaves).persist(any());
    }

    private static Stream<Arguments> getBookTicketsArgs() {
        return Stream.of(
                Arguments.of(createUser(), createUser(), 1, times(1), times(1)),
                Arguments.of(createUser(), createUser(), 2, times(1), times(2)),
                Arguments.of(createUser(), null,  1, never(), times(1))
        );
    }

    @Test
    void bookTickets() {
        bookingService.bookTickets(createTickets(null, event, 3));
        verify(userService, never()).save(any());
        verify(ticketRepository, never()).persist(any());
    }

    @Test
    void getPurchasedTicketsForEvent() {
        Event event = createEvent("event1");
        Collection<Ticket> expectedTickets = createTickets(
                createTicket(event, dateTime, 1L),
                createTicket(event, dateTime, 2L)
        );
        when(ticketRepository.findAll()).thenReturn(tickets);
        Set<Ticket> purchasedTickets = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        assertThat(purchasedTickets, is(expectedTickets));
    }
}