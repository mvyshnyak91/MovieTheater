package ua.vyshnyak.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import ua.vyshnyak.entities.Auditorium;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.EventRating;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.repository.EventRepository;
import ua.vyshnyak.services.AuditoriumService;
import ua.vyshnyak.services.BookingService;
import ua.vyshnyak.services.EventService;
import ua.vyshnyak.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MovieTheaterCommands implements CommandMarker {

    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @CliCommand(value = "mt enterEvent", help = "")
    public String enterEvent(
            @CliOption(key = "name") final String name,
            @CliOption(key = "basePrice") final String basePrice,
            @CliOption(key = "rating") final String rating) {
        Event event = new Event();
        event.setName(name);
        event.setBasePrice(new BigDecimal(basePrice));
        event.setRating(EventRating.valueOf(rating));
        eventService.save(event);
        return String.format("event %s has been added", name);
    }

    @CliCommand(value = "mt assignAirDates", help = "")
    public String assignAirDates(
            @CliOption(key = "eventName") final String eventName,
            @CliOption(key = "airDate") final String airDate,
            @CliOption(key = "auditoriumName") final String auditoriumName) {
        Event event = eventService.getByName(eventName);
        LocalDateTime dateTime = LocalDateTime.parse(airDate);
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        event.addAirDateTime(dateTime, auditorium);
        eventRepository.update(event);
        return String.format("airDate for event %s has been assigned", eventName);
    }

    @CliCommand(value = "mt viewEvents", help = "")
    public String viewEvents() {
        return eventService.getAll().toString();
    }

    @CliCommand(value = "mt register", help = "")
    public String register(
            @CliOption(key = "firstName") final String firstName,
            @CliOption(key = "lastName") final String lastName,
            @CliOption(key = "email") final String email,
            @CliOption(key = "dateOfBirth") final String dateOfBirth) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDateOfBirth(LocalDate.parse(dateOfBirth));
        userService.save(user);
        return "user has been successfully registered";
    }

    @CliCommand(value = "mt getTicketPrice", help = "")
    public String getTicketPrice(
            @CliOption(key = "eventName") final String eventName,
            @CliOption(key = "airDate") final String airDate,
            @CliOption(key = "email") final String email,
            @CliOption(key = "seats") final String seats) {
        Event event = eventService.getByName(eventName);
        User user = userService.getUserByEmail(email);
        LocalDateTime dateTime = LocalDateTime.parse(airDate);
        Set<Long> seatSet = Stream.of(seats.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        BigDecimal ticketPrice = bookingService.getTicketsPrice(event, dateTime, user, seatSet);
        return ticketPrice.toString();
    }

    @CliCommand(value = "mt buy tickets", help = "")
    public String buyTickets(
            @CliOption(key = "eventName") final String eventName,
            @CliOption(key = "airDate") final String airDate,
            @CliOption(key = "email") final String email,
            @CliOption(key = "seats") final String seats) {
        Event event = eventService.getByName(eventName);
        User user = userService.getUserByEmail(email);
        LocalDateTime dateTime = LocalDateTime.parse(airDate);
        Set<Long> seatSet = Stream.of(seats.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        Set<Ticket> tickets = seatSet.stream()
                .map(seat -> new Ticket(user, event, dateTime, seat))
                .collect(Collectors.toSet());
        bookingService.bookTickets(tickets);
        return "tickets have been booked";
    }

    @CliCommand(value = "mt viewPurchasedTickets", help = "")
    public String viewPurchasedTickets(
            @CliOption(key = "eventName") final String eventName,
            @CliOption(key = "airDate") final String airDate) {
        Event event = eventService.getByName(eventName);
        LocalDateTime dateTime = LocalDateTime.parse(airDate);
        Set<Ticket> purchasedTickets = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        return purchasedTickets.toString();
    }
}
