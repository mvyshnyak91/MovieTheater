package ua.vyshnyak.services.impl;

import ua.vyshnyak.entities.User;
import ua.vyshnyak.exceptions.EntityNotFoundException;
import ua.vyshnyak.repository.UserRepository;
import ua.vyshnyak.services.UserService;

import java.util.Collection;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new EntityNotFoundException(""));
    }

    @Override
    public void save(User entity) {
        userRepository.persist(entity);
    }

    @Override
    public void remove(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public User getById(Long id) {
        return userRepository.find(id).orElseThrow(() -> new EntityNotFoundException(""));
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }
}
