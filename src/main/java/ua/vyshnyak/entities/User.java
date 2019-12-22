package ua.vyshnyak.entities;

import org.springframework.shell.support.util.OsUtils;

import java.time.LocalDate;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private NavigableSet<Ticket> tickets = new TreeSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public NavigableSet<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(NavigableSet<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, dateOfBirth);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("firstName: ").append(firstName).append(OsUtils.LINE_SEPARATOR)
                .append("lastName: ").append(lastName).append(OsUtils.LINE_SEPARATOR)
                .append("email: ").append(email).append(OsUtils.LINE_SEPARATOR)
                .append("dateOfBirth: ").append(dateOfBirth).toString();
    }
}
