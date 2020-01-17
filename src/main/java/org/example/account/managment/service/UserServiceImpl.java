package org.example.account.managment.service;

import org.example.account.managment.entity.User;
import org.example.account.managment.exception.NotEnoughFundsException;

public class UserServiceImpl implements UserService {

    public boolean transfer(User sender, User recipient, double amount) throws NotEnoughFundsException {
        if (sender.getAmount() < amount){
            throw new NotEnoughFundsException();
        }
        sender.setAmount(sender.getAmount() - amount);
        recipient.setAmount(recipient.getAmount() + amount);
        return true;
    }
}
