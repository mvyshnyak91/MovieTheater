package ua.vyshnyak.repository.impl.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static ua.vyshnyak.services.impl.TestUtils.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.vyshnyak.JdbcTemplateConfig;
import ua.vyshnyak.entities.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcTemplateConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcTemplateUserRepositoryImplTest {

    @Autowired
    private JdbcTemplateUserRepositoryImpl jdbcTemplateUserRepositoryImpl;

    @Test
    void getUserByEmail() {
        User user = createUser("email@test.com.ua");
        jdbcTemplateUserRepositoryImpl.persist(user);
        assertThat(user.getId(), is(notNullValue()));

        Optional<User> expectedUser = jdbcTemplateUserRepositoryImpl.getUserByEmail("email@test.com.ua");

        assertTrue(expectedUser.isPresent());
        assertThat(expectedUser.get(), is(user));
    }

    @Test
    void getUserByEmailNotFound() {
        Optional<User> expectedUser = jdbcTemplateUserRepositoryImpl.getUserByEmail("not-existing@com.ua");
        assertFalse(expectedUser.isPresent());
    }

    @Test
    void persist() {
        User user = createUser("email@test.com.ua");
        jdbcTemplateUserRepositoryImpl.persist(user);
        assertThat(user.getId(), is(notNullValue()));

        Optional<User> expectedUser = jdbcTemplateUserRepositoryImpl.find(user.getId());

        assertTrue(expectedUser.isPresent());
        assertThat(expectedUser.get(), is(user));
    }

    @Test
    void update() {
        User user = createUser("email@test.com.ua");
        jdbcTemplateUserRepositoryImpl.persist(user);
        assertThat(user.getId(), is(notNullValue()));

        Optional<User> persistedUser = jdbcTemplateUserRepositoryImpl.find(user.getId());

        assertTrue(persistedUser.isPresent());
        assertThat(persistedUser.get(), is(user));

        user.setEmail("changed-email@test.com.ua");
        jdbcTemplateUserRepositoryImpl.update(user);

        Optional<User> updatedUser = jdbcTemplateUserRepositoryImpl.find(user.getId());

        assertTrue(updatedUser.isPresent());
        assertThat(updatedUser.get(), is(user));
    }

    @Test
    void find() {
        User user = createUser("email@test.com.ua");
        jdbcTemplateUserRepositoryImpl.persist(user);
        assertThat(user.getId(), is(notNullValue()));

        Optional<User> expectedUser = jdbcTemplateUserRepositoryImpl.find(user.getId());
        assertTrue(expectedUser.isPresent());
        assertThat(expectedUser.get(), is(user));
    }

    @Test
    void find_userNotFound() {
        Optional<User> expectedUser = jdbcTemplateUserRepositoryImpl.find(0L);
        assertFalse(expectedUser.isPresent());
    }

    @ParameterizedTest
    @MethodSource("findAllArguments")
    void findAll(List<User> users, int userCount) {
        users.forEach(jdbcTemplateUserRepositoryImpl::persist);
        Collection<User> allUsers = jdbcTemplateUserRepositoryImpl.findAll();
        assertThat(allUsers.size(), is(userCount));
        assertThat(allUsers, is(users));
    }

    private static Stream<Arguments> findAllArguments() {
        return Stream.of(
                Arguments.of(Collections.emptyList(), 0),
                Arguments.of(Collections.singletonList(createUser()), 1),
                Arguments.of(Arrays.asList(createUser("eamil1"), createUser("email2")), 2)
        );
    }

    @Test
    void delete() {
        User user = createUser("email@test.com.ua");
        jdbcTemplateUserRepositoryImpl.persist(user);
        assertThat(user.getId(), is(notNullValue()));

        Optional<User> persistedUser = jdbcTemplateUserRepositoryImpl.find(user.getId());
        assertTrue(persistedUser.isPresent());
        assertThat(persistedUser.get(), is(user));

        jdbcTemplateUserRepositoryImpl.delete(user);

        Optional<User> deletedUser = jdbcTemplateUserRepositoryImpl.find(user.getId());
        assertFalse(deletedUser.isPresent());
    }
}