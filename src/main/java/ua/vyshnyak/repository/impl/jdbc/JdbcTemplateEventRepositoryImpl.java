package ua.vyshnyak.repository.impl.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.vyshnyak.entities.Event;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.repository.EventRepository;

@Component
public class JdbcTemplateEventRepositoryImpl implements EventRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Event> EVENT_MAPPER = BeanPropertyRowMapper.newInstance(Event.class);

    @Override
    public Optional<Event> getByName(String name) {
        String query = "SELECT * FROM events WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, EVENT_MAPPER, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void persist(Event event) {
        String query = "INSERT INTO events (name, basePrice, rating) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, event.getName());
            ps.setBigDecimal(2, event.getBasePrice());
            ps.setObject(3, event.getRating());
            return ps;
        }, keyHolder);

        event.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Event event) {
        String query = "UPDATE events SET name = ?, basePrice = ?, rating = ? WHERE id = ?";
        jdbcTemplate.update(query, event.getName(), event.getBasePrice(), event.getRating(), event.getId());
    }

    @Override
    public Optional<Event> find(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, EVENT_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Event> findAll() {
        String query = "SELECT * FROM events";
        return jdbcTemplate.query(query, EVENT_MAPPER);
    }

    @Override
    public void delete(Event event) {
        String query = "DELETE FROM events WHERE id = ?";
        jdbcTemplate.update(query, event.getId());
    }
}
