package ua.vyshnyak.repository.impl.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.vyshnyak.entities.Ticket;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.repository.TicketRepository;

@Component
public class JdbcTemplateTicketRepositoryImpl implements TicketRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Ticket> TICKET_MAPPER = BeanPropertyRowMapper.newInstance(Ticket.class);

    @Override
    public void persist(Ticket ticket) {
        String query = "INSERT INTO tickets (dateTime, seat, ticketPrice) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, ticket.getDateTime());
            ps.setLong(2, ticket.getSeat());
            ps.setBigDecimal(3, ticket.getTicketPrice());
            return ps;
        }, keyHolder);

        ticket.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Ticket ticket) {
        String query = "UPDATE users SET dateTime = ?, seat = ?, ticketPrice = ? WHERE id = ?";
        jdbcTemplate.update(query, ticket.getDateTime(), ticket.getSeat(),
                ticket.getTicketPrice(), ticket.getId());
    }

    @Override
    public Optional<Ticket> find(Long id) {
        String query = "SELECT * FROM tickets WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, TICKET_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Ticket> findAll() {
        String query = "SELECT * FROM tickets";
        return jdbcTemplate.query(query, TICKET_MAPPER);
    }

    @Override
    public void delete(Ticket ticket) {
        String query = "DELETE FROM tickets WHERE id = ?";
        jdbcTemplate.update(query, ticket.getId());
    }
}
