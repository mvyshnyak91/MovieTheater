package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import ua.vyshnyak.entities.*;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.exceptions.SeatNotAvailableException;
import ua.vyshnyak.repository.TicketRepository;
import ua.vyshnyak.repository.UserRepository;
import ua.vyshnyak.services.DiscountService;
import ua.vyshnyak.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static ua.vyshnyak.entities.EventRating.HIGH;
import static ua.vyshnyak.entities.EventRating.LOW;
import static ua.vyshnyak.entities.EventRating.MID;
import static ua.vyshnyak.services.impl.TestUtils.*;
import static ua.vyshnyak.services.impl.TestUtils.dateTime;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Spy
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private DiscountService discountService;
    @Mock
    private UserService userService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;

    private Event event;
    private User user;
    private NavigableSet<Ticket> tickets;

    @BeforeEach
    void setUp() {
        event = createEvent(createAuditorium("Auditorium", 10, createSeats(8L, 9L, 10L)));
        user = createUser();
        tickets = createTickets(
                createTicket(user, createEvent("event1"), dateTime, 1L),
                createTicket(user, createEvent("event1"), dateTime, 2L),
                createTicket(user, createEvent("event1"), dateTime.plusDays(1), 1L),
                createTicket(user, createEvent("event2"), dateTime, 1L)
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

    @Test
    void getTicketsPriceWrongAirDate() {
        assertThrows(IllegalStateException.class,
                () -> bookingService.getTicketsPrice(event, dateTime.plusDays(1), user, createSeats(1L, 2L)));

        verifyNoMoreInteractions(discountService);
    }

    @Test
    void getTicketsPriceSeatIsNotAvailable() {
        Set<Ticket> tickets = createTickets(
                createTicket(createUser(), 1L),
                createTicket(null, 2L)
        );
        doReturn(tickets).when(bookingService).getPurchasedTicketsForEvent(event, dateTime);

        assertThrows(SeatNotAvailableException.class,
                () -> bookingService.getTicketsPrice(event, dateTime, user, createSeats(1L, 2L)));

        verifyNoMoreInteractions(discountService);
    }

    @Test
    void getAvailableSeats() {
        Set<Ticket> tickets = createTickets(
                createTicket(createUser(), 1L),
                createTicket(null, 2L)
        );
        Set<Long> expectedAvailableSeats = createSeats(3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
        doReturn(tickets).when(bookingService).getPurchasedTicketsForEvent(event, dateTime);

        Set<Long> availableTickets = bookingService.getAvailableSeats(event, dateTime);

        assertThat(availableTickets, is(expectedAvailableSeats));
        verify(bookingService).getPurchasedTicketsForEvent(event, dateTime);
    }

    @Test
    void bookTicket() {
        User user = createUser();
        Ticket ticket = createTicket(user, 1L);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);

        bookingService.bookTicket(ticket);

        assertThat(user.getTickets().contains(ticket), is(true));
        verify(userRepository, times(1)).update(user);
        verify(ticketRepository, times(1)).persist(ticket);
    }

    @Test
    void bookTicketUserIsNotRegistered() {
        User user = createUser();
        Ticket ticket = createTicket(user, 1L);
        when(userService.getUserByEmail(user.getEmail())).thenThrow(EntityNotFoundException.class);

        assertDoesNotThrow(() -> bookingService.bookTicket(ticket));

        assertThat(user.getTickets(), is(empty()));
        verify(userRepository, never()).update(user);
        verify(ticketRepository, times(1)).persist(ticket);
    }

    @Test
    void bookTicketSeatIsNotAvailable() {
        Ticket ticket = createTicket(user, event, 1L);
        doReturn(Collections.singleton(ticket))
                .when(bookingService).getPurchasedTicketsForEvent(event, ticket.getDateTime());

        assertThrows(SeatNotAvailableException.class, () -> bookingService.bookTicket(ticket));
        
        assertThat(user.getTickets(), is(empty()));
        verify(userRepository, never()).update(user);
        verify(ticketRepository, never()).persist(ticket);
    }

    @Test
    void bookTicketWrongAirDate() {
        Ticket ticket = createTicket(user, event, 1L);
        ticket.setDateTime(LocalDateTime.now());

        assertThrows(IllegalStateException.class, () -> bookingService.bookTicket(ticket));

        assertThat(user.getTickets(), is(empty()));
        verify(userRepository, never()).update(user);
        verify(ticketRepository, never()).persist(ticket);
    }

    @Test
    void bookTickets() {
        Set<Ticket> tickets = createTickets(
                createTicket(createUser(), 1L),
                createTicket(null, 2L)
        );
        doNothing().when(bookingService).bookTicket(any(Ticket.class));

        bookingService.bookTickets(tickets);

        verify(bookingService, times(1)).bookTicket(any(Ticket.class));
    }

    @Test
    void getPurchasedTicketsForEvent() {
        Event event = createEvent("event1");
        Collection<Ticket> expectedTickets = createTickets(
                createTicket(user, event, dateTime, 1L),
                createTicket(user, event, dateTime, 2L)
        );
        when(ticketRepository.findAll()).thenReturn(tickets);
        Set<Ticket> purchasedTickets = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        assertThat(purchasedTickets, is(expectedTickets));
    }
}