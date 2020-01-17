package org.example.account.managment.dao;

import org.example.account.managment.entity.User;
import org.example.account.managment.exception.UserNotFoundException;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {

    private final Map<Long,User> users;

    public UserRepositoryImpl(URI uri){
        users = new HashMap<>();
    }


    @Override
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(Long id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null){
            throw new UserNotFoundException();
        }
        return users.get(id);
    }

    @Override
    public boolean update(User user) throws UserNotFoundException {
        if (users.replace(user.getId(), user) == null){
            throw new UserNotFoundException();
        }
        return true;
    }

    @Override
    public User save(User user) {
        if (user == null){
            throw new NullPointerException("User is null");
        }
        if (user.getId() == null){
            throw new NullPointerException("User id is null");

        }
        return users.put(user.getId(), user);
    }

    @Override
    public User delete(User user) {
        return users.remove(user.getId());
    }
}
