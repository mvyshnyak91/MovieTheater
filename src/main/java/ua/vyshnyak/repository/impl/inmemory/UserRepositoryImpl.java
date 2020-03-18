package ua.vyshnyak.repository.impl.inmemory;

import org.springframework.stereotype.Component;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.repository.UserRepository;

import java.util.Optional;

@Component
//@Profile("default")
public class UserRepositoryImpl extends AbstractCrudRepository<User> implements UserRepository {
    @Override
    public Optional<User> getUserByEmail(String email) {
        return entities.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
