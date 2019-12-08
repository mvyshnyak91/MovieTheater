package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vyshnyak.entities.Auditorium;
import ua.vyshnyak.services.AuditoriumService;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ua.vyshnyak.services.impl.TestUtils.createAuditorium;
import static ua.vyshnyak.services.impl.TestUtils.createAuditoriums;

class AuditoriumServiceImplTest {
    private AuditoriumService auditoriumService;
    private Set<Auditorium> auditoriums;
    @BeforeEach
    void setUp() {
        auditoriums = createAuditoriums(
                createAuditorium("First Auditorium"),
                createAuditorium("Second Auditorium")
        );
        auditoriumService = new AuditoriumServiceImpl(auditoriums);
    }
    @Test
    void getAll() {
        Set<Auditorium> all = auditoriumService.getAll();
        assertThat(all, is(auditoriums));
    }

    @Test
    void getByName() {
        Auditorium expectedAuditorium = createAuditorium("First Auditorium");
        Auditorium auditorium = auditoriumService.getByName("First Auditorium");
        assertThat(auditorium, is(expectedAuditorium));
    }

}