package ua.vyshnyak;

import ua.vyshnyak.entities.Auditorium;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.EventRating;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestUtils {
    public static final LocalDateTime airDateTime = LocalDateTime.now();

    private TestUtils() {

    }

    public static Set<Long> createSeats(Long ... seats) {
        return Stream.of(seats).collect(Collectors.toSet());
    }

    public static Event createEvent(Auditorium auditorium) {
        return createEvent("Event", EventRating.MID, auditorium);
    }

    public static Event createEvent(String name) {
        return createEvent(name, EventRating.MID, createAuditorium("Event"));
    }

    public static Event createEvent(String name, EventRating eventRating, Auditorium auditorium) {
        return createEvent(name, airDateTime, eventRating, auditorium, new BigDecimal("10.00"));
    }

    public static Event createEvent(String name, LocalDateTime dateTime, EventRating eventRating,
                                    Auditorium auditorium, BigDecimal basePrice) {
        Event event = new Event();
        event.setName(name);
        event.addAirDateTime(dateTime, auditorium);
        event.setRating(eventRating);
        event.setBasePrice(basePrice);
        return event;
    }

    public static Auditorium createAuditorium(String name) {
        return createAuditorium(name, 10, createSeats(8L, 9L, 10L));
    }

    public static Auditorium createAuditorium(String name, long numberOfSeat, Set<Long> vipSeats) {
        Auditorium auditorium = new Auditorium();
        auditorium.setName(name);
        auditorium.setNumberOfSeats(numberOfSeat);
        auditorium.setVipSeats(vipSeats);
        return auditorium;
    }

    public static Set<Auditorium> createAuditoriums(Auditorium ... auditoriums) {
        return Stream.of(auditoriums).collect(Collectors.toSet());
    }

    public static Set<Ticket> createTickets(User user, Event event, long numberOfTickets) {
        return Stream.iterate(1, x -> x + 1)
                .limit(numberOfTickets)
                .map(x -> new Ticket(user, event, airDateTime, x))
                .collect(Collectors.toSet());
    }

    public static Ticket createTicket(Long seat) {
        return new Ticket(null, createEvent("Event"), airDateTime, seat);
    }

    public static Ticket createTicket(User user, Long seat) {
        return new Ticket(user, createEvent("Event"), airDateTime, seat);
    }

    public static Ticket createTicket(User user, Event event, Long seat) {
        return createTicket(user, event, airDateTime, seat);
    }

    public static Ticket createTicket(User user, Event event, LocalDateTime dateTime, Long seat) {
        return new Ticket(user, event, dateTime, seat);
    }

    public static NavigableSet<Ticket> createTickets(Ticket ... tickets) {
        return Stream.of(tickets).collect(Collectors.toCollection(TreeSet::new));
    }

    public static User createUser() {
        User user = new User();
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail("test_email@test.com");
        return user;
    }

    public static User createUser(LocalDate dateOfBirth) {
        User user = createUser();
        user.setDateOfBirth(dateOfBirth);
        return user;
    }

    public static User createUser(NavigableSet<Ticket> tickets) {
        User user = createUser();
        user.setTickets(tickets);
        return user;
    }

}
