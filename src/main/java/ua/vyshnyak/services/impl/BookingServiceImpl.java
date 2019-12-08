package ua.vyshnyak.services.impl;

import ua.vyshnyak.entities.*;
import ua.vyshnyak.repository.TicketRepository;
import ua.vyshnyak.repository.UserRepository;
import ua.vyshnyak.services.BookingService;
import ua.vyshnyak.services.DiscountService;
import ua.vyshnyak.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

public class BookingServiceImpl implements BookingService {
    private TicketRepository ticketRepository;
    private UserService userService;
    private UserRepository userRepository;
    private DiscountService discountService;

    @Override
    public BigDecimal getTicketsPrice(Event event, LocalDateTime dateTime, User user, Set<Long> seats) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        long numberOfTickets = 0;
        for (Long seat : seats) {
            numberOfTickets++;
            BigDecimal currentPrice = getBasePrice(event, dateTime, seat);
            BigDecimal discountPercent = discountService.getDiscountPercent(user, dateTime, numberOfTickets);
            currentPrice = currentPrice.subtract(getDiscount(currentPrice, discountPercent));
            totalPrice = totalPrice.add(currentPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return totalPrice;
    }

    private BigDecimal getBasePrice(Event event, LocalDateTime dateTime, Long seat) {
        Auditorium auditorium = event.getAuditoriums().get(dateTime);
        BigDecimal basePrice = event.getBasePrice();
        if (auditorium.getVipSeats().contains(seat)) {
            return basePrice.multiply(new BigDecimal("2.00"));
        }
        if (event.getRating() == EventRating.HIGH) {
            return basePrice.multiply(new BigDecimal("1.20"));
        }
        return basePrice;
    }


    private static BigDecimal getDiscount(BigDecimal base, BigDecimal percent){
        return base.multiply(percent.divide(new BigDecimal("100.00")));
    }

    @Override
    public void bookTickets(Set<Ticket> tickets) {
        Map<User, Set<Ticket>> userTickets = tickets.stream()
                .filter(ticket -> ticket.getUser() != null)
                .collect(groupingBy(Ticket::getUser, toCollection(TreeSet::new)));
        userTickets.forEach(this::bookTickets);
    }

    private void bookTickets(User user, Set<Ticket> tickets) {
        Optional<User> persistedUser = userRepository.getUserByEmail(user.getEmail());
        if (persistedUser.isPresent()) {
            persistedUser.get().getTickets().addAll(tickets);
            userService.save(persistedUser.get());
        }
        tickets.forEach(ticketRepository::persist);
    }

    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime) {
        return ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getEvent().equals(event))
                .filter(ticket -> ticket.getDateTime().equals(dateTime))
                .collect(Collectors.toSet());
    }
}
