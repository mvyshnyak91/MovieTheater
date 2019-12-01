package ua.vyshnyak.services;

import ua.vyshnyak.entities.Auditorium;

import java.util.Set;

public interface AuditoriumService {
    Set<Auditorium> getAll();
    Auditorium getByName(String name);
}
