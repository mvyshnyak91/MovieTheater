package ua.vyshnyak.repository;

import ua.vyshnyak.entities.User;

import java.util.Optional;

public interface UserRepository extends GenericCrudRepository<User> {
    Optional<User> getUserByEmail(String email);
}
