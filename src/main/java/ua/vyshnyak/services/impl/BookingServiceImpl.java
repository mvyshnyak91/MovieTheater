package ua.vyshnyak.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ua.vyshnyak.entities.*;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.exceptions.SeatNotAvailableException;
import ua.vyshnyak.repository.TicketRepository;
import ua.vyshnyak.repository.UserRepository;
import ua.vyshnyak.services.BookingService;
import ua.vyshnyak.services.DiscountService;
import ua.vyshnyak.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {
    private static final String VIP_RATE = "2";
    private static final String HIGH_EVENT_RATING_RATE = "1.2";

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DiscountService discountService;

    @Override
    public BigDecimal getTicketsPrice(Event event, LocalDateTime airDateTime, User user, Set<Long> seats) {
        checkEventAirsOnDate(event, airDateTime);
        BigDecimal totalPrice = BigDecimal.ZERO;
        long numberOfTickets = 0;
        for (Long seat : seats) {
            numberOfTickets++;
            checkSeatAvailability(event, airDateTime, seat);
            BigDecimal currentPrice = getBasePrice(event, airDateTime, seat);
            BigDecimal discountPercent = discountService.getDiscountPercent(user, airDateTime, numberOfTickets);
            currentPrice = currentPrice.subtract(getDiscountAmount(currentPrice, discountPercent));
            totalPrice = totalPrice.add(currentPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return totalPrice;
    }

    private BigDecimal getBasePrice(Event event, LocalDateTime airDateTime, Long seat) {
        Auditorium auditorium = event.getAuditoriums().get(airDateTime);
        BigDecimal basePrice = event.getBasePrice();
        if (auditorium.getVipSeats().contains(seat)) {
            return basePrice.multiply(new BigDecimal(VIP_RATE));
        }
        if (event.getRating() == EventRating.HIGH) {
            return basePrice.multiply(new BigDecimal(HIGH_EVENT_RATING_RATE));
        }
        return basePrice;
    }


    private static BigDecimal getDiscountAmount(BigDecimal base, BigDecimal percent){
        return base.multiply(percent.divide(new BigDecimal("100.00")));
    }

    @Override
    public void bookTickets(Set<Ticket> tickets) {
        tickets.stream()
                .filter(ticket -> ticket.getUser() != null)
                .forEach(this::bookTicket);
    }

    @Override
    public void bookTicket(Ticket ticket) {
        validateTicket(ticket);
        try {
            User user = userService.getUserByEmail(ticket.getUser().getEmail());
            ticket.setUser(user);
            user.getTickets().add(ticket);
            userRepository.update(user);
        } catch (EntityNotFoundException ignored) {

        }
        ticketRepository.persist(ticket);
    }

    private void validateTicket(Ticket ticket) {
        checkEventAirsOnDate(ticket.getEvent(), ticket.getDateTime());
        checkSeatAvailability(ticket.getEvent(), ticket.getDateTime(), ticket.getSeat());
    }

    private void checkEventAirsOnDate(Event event, LocalDateTime airDateTime) {
        if (!event.airsOnDateTime(airDateTime)) {
            throw new IllegalStateException("Event does not air on dateTime " + airDateTime);
        }
    }

    private void checkSeatAvailability(Event event, LocalDateTime airDateTime, Long seat) {
        Set<Long> availableSeats = getAvailableSeats(event, airDateTime);
        if (!availableSeats.contains(seat)) {
            throw new SeatNotAvailableException(String.format("Seat %s is not available", seat));
        }
    }

    @Override
    public Set<Long> getAvailableSeats(Event event, LocalDateTime airDateTime) {
        checkEventAirsOnDate(event, airDateTime);
        Set<Long> availableSeats = event.getAuditoriums().get(airDateTime).getAllSeats();
        Set<Long> purchasedSeats = getPurchasedSeats(event, airDateTime);
        availableSeats.removeAll(purchasedSeats);
        return availableSeats;
    }

    private Set<Long> getPurchasedSeats(Event event, LocalDateTime airDateTime) {
        return getPurchasedTicketsForEvent(event, airDateTime).stream()
                .map(Ticket::getSeat)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime airDateTime) {
        checkEventAirsOnDate(event, airDateTime);
        return ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getEvent().equals(event))
                .filter(ticket -> ticket.getDateTime().equals(airDateTime))
                .collect(Collectors.toSet());
    }
}
