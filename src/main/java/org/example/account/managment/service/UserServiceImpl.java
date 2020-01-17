package org.example.account.managment.service;

import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.entity.User;
import org.example.account.managment.exception.IllegalRecipientException;
import org.example.account.managment.exception.NotEnoughFundsException;
import org.example.account.managment.exception.UserNotFoundException;

public class UserServiceImpl implements UserService {

    UserRepository userRepository = Configuration.getUserRepository();

    public boolean transfer(User sender, User recipient, long amount) throws NotEnoughFundsException, IllegalRecipientException {
        if (sender == null){
            throw new NullPointerException("Sender cannot be Null");
        }
        if (recipient == null){
            throw new NullPointerException("Recipient cannot be Null");
        }
        if (sender == recipient){
            throw new IllegalRecipientException("User cannot send money to itself");
        }
        if (amount < 0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (sender.getAmount() < amount){
            throw new NotEnoughFundsException("User don't have enough money");
        }
        sender.setAmount(sender.getAmount() - amount);
        recipient.setAmount(recipient.getAmount() + amount);
        return true;
    }

    public User getUser(Long id) throws UserNotFoundException {
        return userRepository.get(id);
    }
}
