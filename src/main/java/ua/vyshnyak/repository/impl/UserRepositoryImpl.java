package ua.vyshnyak.repository.impl;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl extends AbstractCrudRepository<User> implements UserRepository {
    @Override
    public Optional<User> getUserByEmail(String email) {
        return entities.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
