package ua.vyshnyak.repository.impl;

import org.junit.jupiter.api.Test;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.impl.TestUtils;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class UserRepositoryImplTest extends AbstractCrudRepositoryTest<User, UserRepositoryImpl> {

    @Test
    void getUserByEmail() {
        User user = createEntity(1L);
        user.setEmail("test_email@test.com");
        getRepository().entities.put(user.getId(), user);
        Optional<User> user1 = getRepository().getUserByEmail("test_email@test.com");
        assertThat(user1.isPresent(), is(true));
        assertThat(user1.get(), is(user));
    }

    @Override
    public UserRepositoryImpl createRepository() {
        return new UserRepositoryImpl();
    }

    @Override
    public User createEntity(Long id) {
        User user = TestUtils.createUser();
        user.setId(id);
        return user;
    }
}