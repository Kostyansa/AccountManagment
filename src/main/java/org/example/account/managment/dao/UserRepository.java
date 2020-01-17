package org.example.account.managment.dao;

import org.example.account.managment.entity.User;
import org.example.account.managment.exception.UserNotFoundException;

import java.util.List;

public interface UserRepository {

    List<User> get();

    User get(Long id) throws UserNotFoundException;

    boolean update(User user) throws UserNotFoundException;

    User save(User user);

    User delete(User user);
}
