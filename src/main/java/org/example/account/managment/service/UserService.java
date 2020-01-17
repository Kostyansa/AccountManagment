package org.example.account.managment.service;

import org.example.account.managment.entity.User;
import org.example.account.managment.exception.IllegalRecipientException;
import org.example.account.managment.exception.NotEnoughFundsException;
import org.example.account.managment.exception.UserNotFoundException;

public interface UserService {

    boolean transfer(User sender, User recipient, double amount) throws NotEnoughFundsException, IllegalRecipientException;

    User getUser(Long id) throws UserNotFoundException;
}
