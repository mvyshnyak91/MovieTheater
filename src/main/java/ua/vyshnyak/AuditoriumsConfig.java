package ua.vyshnyak;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import ua.vyshnyak.entities.Auditorium;

@Configuration
@PropertySource("classpath:auditoriums.properties")
public class AuditoriumsConfig {
    @Autowired
    private Environment environment;
    @Bean
    public Auditorium firstAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setName(environment.getProperty("first.auditorium.name"));
        auditorium.setNumberOfSeats(environment.getProperty("first.auditorium.numberOfSeats", Integer.class));
        auditorium.setVipSeats(Stream.of(environment.getProperty("first.auditorium.vipSeats")
                .split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        return auditorium;
    }

    @Bean
    public Auditorium secondAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setName(environment.getProperty("second.auditorium.name"));
        auditorium.setNumberOfSeats(environment.getProperty("second.auditorium.numberOfSeats", Integer.class));
        auditorium.setVipSeats(Stream.of(environment.getProperty("second.auditorium.vipSeats")
                .split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        return auditorium;
    }
}
