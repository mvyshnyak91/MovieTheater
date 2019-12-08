package ua.vyshnyak.services.impl;

import ua.vyshnyak.entities.Auditorium;
import ua.vyshnyak.services.AuditoriumService;

import java.util.List;
import java.util.Set;

public class AuditoriumServiceImpl implements AuditoriumService {
    private Set<Auditorium> auditoriums;

    public AuditoriumServiceImpl(Set<Auditorium> auditoriums) {
        this.auditoriums = auditoriums;
    }

    @Override
    public Set<Auditorium> getAll() {
        return auditoriums;
    }

    @Override
    public Auditorium getByName(String name) {
        return auditoriums.stream()
                .filter(auditorium -> auditorium.getName().equals(name))
                .findFirst()
                .get();
    }
}
