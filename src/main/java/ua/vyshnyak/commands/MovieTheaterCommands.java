package ua.vyshnyak.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;
import ua.vyshnyak.entities.Auditorium;
import ua.vyshnyak.entities.BaseEntity;
import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.EventRating;
import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.repository.EventRepository;
import ua.vyshnyak.services.AuditoriumService;
import ua.vyshnyak.services.BookingService;
import ua.vyshnyak.services.EventService;
import ua.vyshnyak.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
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

    private boolean adminPanelEnabled;

    @CliAvailabilityIndicator({"mt register", "mt viewEvents", "mt getTicketPrice", "mt buyTickets", "mt enterAdminPanel"})
    public boolean isUserCommandAvailable() {
        return !adminPanelEnabled;
    }

    @CliAvailabilityIndicator({"mt enterEvent", "mt assignAirDates", "mt viewPurchasedTickets", "mt viewUsers", "mt exitAdminPanel"})
    public boolean isAdminCommandAvailable() {
        return adminPanelEnabled;
    }

    @CliCommand(value = "mt enterAdminPanel", help = "Enable admin commands")
    public String adminEnable() {
        adminPanelEnabled = true;
        return "Admin commands enabled.";
    }

    @CliCommand(value = "mt exitAdminPanel", help = "Disable admin commands")
    public String exitAdminPanel() {
        adminPanelEnabled = false;
        return "Admin commands disabled";
    }

    @CliCommand(value = "mt enterEvent", help = "Create new event")
    public String enterEvent(
            @CliOption(key = "name") String name,
            @CliOption(key = "basePrice") String basePrice,
            @CliOption(key = "rating") String rating) {
        Event event = new Event();
        event.setName(name);
        event.setBasePrice(new BigDecimal(basePrice));
        event.setRating(EventRating.valueOf(rating));
        eventService.save(event);
        return String.format("event %s has been added", name);
    }

    @CliCommand(value = "mt assignAirDates", help = "Add air date and auditorium to event")
    public String assignAirDates(
            @CliOption(key = "eventName") String eventName,
            @CliOption(key = "airDate") String airDate,
            @CliOption(key = "auditoriumName") String auditoriumName) {
        Event event = eventService.getByName(eventName);
        LocalDateTime dateTime = LocalDateTime.parse(airDate);
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        event.addAirDateTime(dateTime, auditorium);
        eventRepository.update(event);
        return String.format("airDate for event %s has been assigned", eventName);
    }

    @CliCommand(value = "mt viewEvents", help = "View all events")
    public String viewEvents() {
        return toString(eventService.getAll());
    }

    @CliCommand(value = "mt viewUsers", help = "View all registered Users")
    public String viewUsers() {
        return toString(userService.getAll());
    }

    @CliCommand(value = "mt register", help = "Register new user")
    public String register(
            @CliOption(key = "firstName") String firstName,
            @CliOption(key = "lastName") String lastName,
            @CliOption(key = "email") String email,
            @CliOption(key = "dateOfBirth") String dateOfBirth) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDateOfBirth(LocalDate.parse(dateOfBirth));
        userService.save(user);
        return "user has been successfully registered";
    }

    @CliCommand(value = "mt getTicketPrice", help = "Calculate total price for ticket")
    public String getTicketPrice(
            @CliOption(key = "eventName") String eventName,
            @CliOption(key = "airDate") String airDate,
            @CliOption(key = "email") String email,
            @CliOption(key = "seats") String seats) {
        Event event = eventService.getByName(eventName);
        User user = userService.getUserByEmail(email);
        LocalDateTime dateTime = LocalDateTime.parse(airDate);
        Set<Long> seatSet = Stream.of(seats.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        BigDecimal ticketPrice = bookingService.getTicketsPrice(event, dateTime, user, seatSet);
        return ticketPrice.toString();
    }

    @CliCommand(value = "mt buyTickets", help = "Buy tickets")
    public String buyTickets(
            @CliOption(key = "eventName") String eventName,
            @CliOption(key = "airDate") String airDate,
            @CliOption(key = "email") String email,
            @CliOption(key = "seats") String seats) {
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

    @CliCommand(value = "mt viewPurchasedTickets", help = "View all purchased tickets for specific event and air date")
    public String viewPurchasedTickets(
            @CliOption(key = "eventName") String eventName,
            @CliOption(key = "airDate") String airDate) {
        Event event = eventService.getByName(eventName);
        LocalDateTime dateTime = LocalDateTime.parse(airDate);
        Set<Ticket> purchasedTickets = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        return toString(purchasedTickets);
    }

    private <T extends BaseEntity> String toString(Collection<T> entities) {
        StringBuilder text = new StringBuilder();
        for (BaseEntity entity : entities) {
            text.append(OsUtils.LINE_SEPARATOR);
            text.append("========================");
            text.append(OsUtils.LINE_SEPARATOR);
            text.append(entity);
            text.append(OsUtils.LINE_SEPARATOR);
            text.append("========================");
        }
        return text.toString();
    }
}
