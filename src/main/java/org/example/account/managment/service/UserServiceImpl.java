package org.example.account.managment.service;

import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.entity.User;
import org.example.account.managment.exception.IllegalRecipientException;
import org.example.account.managment.exception.NotEnoughFundsException;
import org.example.account.managment.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository = Configuration.getUserRepository();

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
        logger.trace(String.format("Before transaction sender amount: %s, recipient amount: %s", sender.getAmount(), recipient.getAmount()));
        logger.info(String.format("sent amount: %s from %s to %s", amount, sender.getId(), recipient.getId()));
        sender.setAmount(sender.getAmount() - amount);
        if (sender.getAmount() < 0){
            logger.warn("Sender amount is less than zero");
        }
        recipient.setAmount(recipient.getAmount() + amount);
        logger.trace(String.format("After transaction sender amount: %s, recipient amount: %s", sender.getAmount(), recipient.getAmount()));
        return true;
    }

    public User getUser(Long id) throws UserNotFoundException {
        return userRepository.get(id);
    }
}
