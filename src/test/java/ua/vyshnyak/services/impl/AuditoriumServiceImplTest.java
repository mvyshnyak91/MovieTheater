package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.vyshnyak.AppConfiguration;
import ua.vyshnyak.entities.Auditorium;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.services.AuditoriumService;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.vyshnyak.services.impl.TestUtils.createAuditorium;
import static ua.vyshnyak.services.impl.TestUtils.createAuditoriums;
import static ua.vyshnyak.services.impl.TestUtils.createSeats;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfiguration.class)
class AuditoriumServiceImplTest {

    @Autowired
    private AuditoriumService auditoriumService;

    private Set<Auditorium> auditoriums;

    @BeforeEach
    void setUp() {
        auditoriums = createAuditoriums(
                createAuditorium("test1", 10, createSeats(8L, 9L, 10L)),
                createAuditorium("test2", 15, createSeats(13L, 14L, 15L))
        );
    }
    @Test
    void getAll() {
        Set<Auditorium> allAuditoriums = auditoriumService.getAll();
        assertThat(allAuditoriums, is(auditoriums));
    }

    @Test
    void getByName() {
        Auditorium expectedAuditorium = createAuditorium("test1", 10, createSeats(8L, 9L, 10L));
        Auditorium auditorium = auditoriumService.getByName("test1");
        assertThat(auditorium, is(expectedAuditorium));
    }

    @Test
    void getByNameNoAuditoriumUnderName() {
        assertThrows(EntityNotFoundException.class, () -> auditoriumService.getByName("Wrong Auditorium"));
    }
}