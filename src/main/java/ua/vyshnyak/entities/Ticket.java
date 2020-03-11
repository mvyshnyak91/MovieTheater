package ua.vyshnyak.entities;

import org.springframework.shell.support.util.OsUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket extends BaseEntity implements Comparable<Ticket> {
    private User user;
    private Event event;
    private LocalDateTime dateTime;
    private long seat;
    private BigDecimal ticketPrice;

    public Ticket(User user, Event event, LocalDateTime dateTime, long seat) {
        this.user = user;
        this.event = event;
        this.dateTime = dateTime;
        this.seat = seat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getSeat() {
        return seat;
    }

    public void setSeat(long seat) {
        this.seat = seat;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public int compareTo(Ticket other) {
        if (other == null) {
            return 1;
        }
        int result = dateTime.compareTo(other.getDateTime());

        if (result == 0) {
            result = event.getName().compareTo(other.getEvent().getName());
        }
        if (result == 0) {
            result = Long.compare(seat, other.getSeat());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return seat == ticket.seat &&
                Objects.equals(event, ticket.event) &&
                Objects.equals(dateTime, ticket.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, dateTime, seat);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("userEmail: ").append(user.getEmail()).append(OsUtils.LINE_SEPARATOR)
                .append("event: ").append(event.getName()).append(OsUtils.LINE_SEPARATOR)
                .append("airDate: ").append(dateTime).append(OsUtils.LINE_SEPARATOR)
                .append("seat: ").append(seat).toString();
    }
}
