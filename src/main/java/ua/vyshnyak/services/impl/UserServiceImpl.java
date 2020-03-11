package ua.vyshnyak.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.exceptions.EntityAlreadyExistsException;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.repository.UserRepository;
import ua.vyshnyak.services.UserService;

import java.util.Collection;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No existing user under email " + email));
    }

    @Override
    public void save(User entity) {
        Optional<User> registeredUser = userRepository.getUserByEmail(entity.getEmail());
        if (!registeredUser.isPresent()) {
            userRepository.persist(entity);
        } else {
            throw new EntityAlreadyExistsException("User with specified email already exists");
        }
    }

    @Override
    public void remove(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public User getById(Long id) {
        return userRepository.find(id)
                .orElseThrow(() -> new EntityNotFoundException("No existing user by id " + id));
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }
}
