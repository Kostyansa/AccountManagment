package org.example.account.managment.service;

import org.example.account.managment.entity.User;
import org.example.account.managment.service.exception.NotEnoughFundsException;

public interface UserService {

    boolean transfer(User sender, User recipient, double amount) throws NotEnoughFundsException;
}
