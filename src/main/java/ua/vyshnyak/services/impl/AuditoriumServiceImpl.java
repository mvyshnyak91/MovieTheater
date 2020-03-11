package ua.vyshnyak.services.impl;

import ua.vyshnyak.entities.Auditorium;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.services.AuditoriumService;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditoriumServiceImpl implements AuditoriumService {
    @Autowired
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
                .orElseThrow(() -> new EntityNotFoundException("No auditorium under specified name"));
    }
}
