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
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

    @CliAvailabilityIndicator({"mt register", "mt getTicketPrice", "mt bookTickets", "mt enterAdminPanel"})
    public boolean isUserCommandAvailable() {
        return !adminPanelEnabled;
    }

    @CliAvailabilityIndicator({"mt createEvent", "mt viewAuditorium", "mt viewAuditoriums", "mt assignAirDateTimes",
            "mt viewPurchasedTickets", "mt viewUsers", "mt viewUserTickets", "mt exitAdminPanel"})
    public boolean isAdminCommandAvailable() {
        return adminPanelEnabled;
    }

    @CliCommand(value = "mt enterAdminPanel", help = "Enable admin commands")
    public String enterAdminPanel() {
        adminPanelEnabled = true;
        return "Admin commands enabled.";
    }

    @CliCommand(value = "mt exitAdminPanel", help = "Disable admin commands")
    public String exitAdminPanel() {
        adminPanelEnabled = false;
        return "Admin commands disabled";
    }

    @CliCommand(value = "mt createEvent", help = "Create new event")
    public String createEvent(
            @CliOption(key = "name", mandatory = true) String name,
            @CliOption(key = "basePrice", mandatory = true) String basePrice,
            @CliOption(key = "rating", mandatory = true) String rating) {
        try {
            Event event = new Event();
            event.setName(name);
            event.setBasePrice(new BigDecimal(basePrice));
            event.setRating(EventRating.valueOf(rating));
            eventService.save(event);
        } catch (IllegalArgumentException exception) {
            return "Wrong event rating. Available ratings " + Arrays.toString(EventRating.values());
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return String.format("Event %s has been created", name);
    }

    @CliCommand(value = "mt assignAirDateTimes", help = "Add air dateTime and auditorium to event")
    public String assignAirDateTimes(
            @CliOption(key = "eventName", mandatory = true) String eventName,
            @CliOption(key = "airDate", mandatory = true) String airDate,
            @CliOption(key = "airTime", mandatory = true) String airTime,
            @CliOption(key = "auditoriumName", mandatory = true) String auditoriumName) {
        try {
            Event event = eventService.getByName(eventName);
            LocalDate date = LocalDate.parse(airDate);
            LocalTime time = LocalTime.parse(airTime);
            Auditorium auditorium = auditoriumService.getByName(auditoriumName);
            event.addAirDateTime(LocalDateTime.of(date, time), auditorium);
            eventRepository.update(event);
        } catch (DateTimeParseException exception) {
            return "Wrong dateTime format. Date format [y]-[m]-[d], Time format [h]:[m]";
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return String.format("airDate for event %s has been assigned", eventName);
    }

    @CliCommand(value = "mt viewEvents", help = "View all events")
    public String viewEvents() {
        Collection<Event> events = eventService.getAll();
        return events.isEmpty()? "No available events" : toString(events);
    }

    @CliCommand(value = "mt viewEvent", help = "View event")
    public String viewEvent(@CliOption(key = "eventName", mandatory = true) String eventName) {
        Event event;
        try {
            event = eventService.getByName(eventName);
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return event.toString();
    }

    @CliCommand(value = "mt viewAuditorium", help = "View auditorium")
    public String viewAuditorium(@CliOption(key = "auditoriumName", mandatory = true) String auditoriumName) {
        Auditorium auditorium;
        try {
            auditorium = auditoriumService.getByName(auditoriumName);
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return auditorium.toString();
    }

    @CliCommand(value = "mt viewAvailableSeats", help = "View available seats for event on specific air date time")
    public String viewAvailableSeats(
            @CliOption(key = "eventName", mandatory = true) String eventName,
            @CliOption(key = "airDate", mandatory = true) String airDate,
            @CliOption(key = "airTime", mandatory = true) String airTime) {
        Set<Long> availableSeats;
        try {
            Event event = eventService.getByName(eventName);
            LocalDate date = LocalDate.parse(airDate);
            LocalTime time = LocalTime.parse(airTime);
            availableSeats = bookingService.getAvailableSeats(event, LocalDateTime.of(date, time));
        } catch (DateTimeParseException exception) {
            return "Wrong dateTime format. Date format [y]-[m]-[d], Time format [h]:[m]";
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return availableSeats.isEmpty() ? "No available seats" : availableSeats.toString();
    }

    @CliCommand(value = "mt viewUsers", help = "View all registered Users")
    public String viewUsers() {
        Collection<User> allUsers = userService.getAll();
        return allUsers.isEmpty() ? "No registered users" : toString(allUsers);
    }

    @CliCommand(value = "mt viewUserTickets", help = "View all tickets booked by user")
    public String viewUserTickets(
            @CliOption(key = "email", mandatory = true) String email) {
        User user;
        try {
            user = userService.getUserByEmail(email);
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return user.getTickets().isEmpty() ? "No booked tickets for this user " : toString(user.getTickets());
    }

    @CliCommand(value = "mt viewAuditoriums", help = "View all auditoriums")
    public String viewAuditoriums() {
        Collection<Auditorium> auditoriums = auditoriumService.getAll();
        return auditoriums.isEmpty() ? "No auditoriums available" : toString(auditoriums);
    }

    @CliCommand(value = "mt register", help = "Register new user")
    public String register(
            @CliOption(key = "firstName") String firstName,
            @CliOption(key = "lastName") String lastName,
            @CliOption(key = "email", mandatory = true) String email,
            @CliOption(key = "dateOfBirth") String dateOfBirth) {
        try {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            if (dateOfBirth != null) {
                user.setDateOfBirth(LocalDate.parse(dateOfBirth));
            }
            userService.save(user);
        } catch (DateTimeParseException exception) {
            return "Wrong dateTime format. Date format [y]-[m]-[d], Time format [h]:[m]";
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return "User has been successfully registered";
    }

    @CliCommand(value = "mt getTicketPrice", help = "Calculate total price for ticket")
    public String getTicketPrice(
            @CliOption(key = "eventName", mandatory = true) String eventName,
            @CliOption(key = "airDate", mandatory = true) String airDate,
            @CliOption(key = "airTime", mandatory = true) String airTime,
            @CliOption(key = "email", mandatory = true) String email,
            @CliOption(key = "seats", mandatory = true) String seats) {
        BigDecimal ticketPrice;
        try {
            Event event = eventService.getByName(eventName);
            User user = new User();
            user.setEmail(email);
            LocalDate date = LocalDate.parse(airDate);
            LocalTime time = LocalTime.parse(airTime);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            Set<Long> seatSet = Stream.of(seats.split(",")).map(Long::valueOf).collect(Collectors.toSet());
            ticketPrice = bookingService.getTicketsPrice(event, dateTime, user, seatSet);
        } catch (DateTimeParseException exception) {
            return "Wrong dateTime format. Date format [y]-[m]-[d], Time format [h]:[m]";
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return ticketPrice.toString();
    }

    @CliCommand(value = "mt bookTickets", help = "Buy tickets")
    public String bookTickets(
            @CliOption(key = "eventName", mandatory = true) String eventName,
            @CliOption(key = "airDate", mandatory = true) String airDate,
            @CliOption(key = "airTime", mandatory = true) String airTime,
            @CliOption(key = "email", mandatory = true) String email,
            @CliOption(key = "seats", mandatory = true) String seats) {
        try {
            Event event = eventService.getByName(eventName);
            User user = new User();
            user.setEmail(email);
            LocalDate date = LocalDate.parse(airDate);
            LocalTime time = LocalTime.parse(airTime);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            Set<Long> seatSet = Stream.of(seats.split(",")).map(Long::valueOf).collect(Collectors.toSet());
            Set<Ticket> tickets = seatSet.stream()
                    .map(seat -> new Ticket(user, event, dateTime, seat))
                    .collect(Collectors.toSet());
            bookingService.bookTickets(tickets);
        } catch (DateTimeParseException exception) {
            return "Wrong dateTime format. Date format [y]-[m]-[d], Time format [h]:[m]";
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return "Tickets have been booked";
    }

    @CliCommand(value = "mt viewPurchasedTickets", help = "View all purchased tickets for specific event and air date")
    public String viewPurchasedTickets(
            @CliOption(key = "eventName", mandatory = true) String eventName,
            @CliOption(key = "airDate", mandatory = true) String airDate,
            @CliOption(key = "airTime", mandatory = true) String airTime) {
        Set<Ticket> purchasedTickets;
        try {
            Event event = eventService.getByName(eventName);
            LocalDate date = LocalDate.parse(airDate);
            LocalTime time = LocalTime.parse(airTime);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            purchasedTickets = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        } catch (DateTimeParseException exception) {
            return "Wrong dateTime format. Date format [y]-[m]-[d], Time format [h]:[m]";
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return purchasedTickets.isEmpty() ? "No purchased tickets" : toString(purchasedTickets);
    }

    private <T> String toString(Collection<T> entities) {
        StringBuilder text = new StringBuilder();
        for (T entity : entities) {
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
