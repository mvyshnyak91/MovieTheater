package ua.vyshnyak.entities;

import org.springframework.shell.support.util.OsUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Auditorium {
    private String name;
    private long numberOfSeats;
    private Set<Long> vipSeats = Collections.emptySet();

    public long countVipSeats(Collection<Long> seats) {
        return seats.stream().filter(seat -> vipSeats.contains(seat)).count();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(long numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Set<Long> getAllSeats() {
        return LongStream.range(1, numberOfSeats + 1).boxed().collect(Collectors.toSet());
    }

    public Set<Long> getVipSeats() {
        return vipSeats;
    }

    public void setVipSeats(Set<Long> vipSeats) {
        this.vipSeats = vipSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auditorium auditorium = (Auditorium) o;
        return Objects.equals(name, auditorium.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("name: ").append(name).append(OsUtils.LINE_SEPARATOR)
                .append("numberOfSeats: ").append(numberOfSeats).append(OsUtils.LINE_SEPARATOR)
                .append("vipSeats: ").append(vipSeats).toString();
    }
}
