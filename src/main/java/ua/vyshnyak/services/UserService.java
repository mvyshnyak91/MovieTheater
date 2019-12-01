package ua.vyshnyak.services;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.exceptions.EntityNotFoundException;

public interface UserService extends GenericService<Long, User> {
    User getUserByEmail(String email) throws EntityNotFoundException;
}
