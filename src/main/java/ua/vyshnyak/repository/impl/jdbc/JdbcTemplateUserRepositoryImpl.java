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

import ua.vyshnyak.entities.User;
import ua.vyshnyak.repository.UserRepository;

@Component
public class JdbcTemplateUserRepositoryImpl implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    @Override
    public Optional<User> getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(query, rs -> rs.next() ?
                Optional.of(USER_MAPPER.mapRow(rs, 1)) : Optional.empty(), email);
    }

    @Override
    public void persist(User user) {
        String query = "INSERT INTO users (firstName, lastName, email, dateOfBirth) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setObject(4, user.getDateOfBirth());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(User user) {
        String query = "UPDATE users SET firstName = ?, lastName = ?, email = ?, dateOfBirth = ? WHERE id = ?";
        jdbcTemplate.update(query, user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getDateOfBirth(), user.getId());
    }

    @Override
    public Optional<User> find(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(query, rs -> rs.next() ?
                Optional.of(USER_MAPPER.mapRow(rs, 1)) : Optional.empty(), id);
    }

    @Override
    public Collection<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, USER_MAPPER);
    }

    @Override
    public void delete(User user) {
        String query = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(query, user.getId());
    }
}
