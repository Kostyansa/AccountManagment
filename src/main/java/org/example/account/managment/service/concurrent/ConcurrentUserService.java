package org.example.account.managment.service.concurrent;

import org.example.account.managment.entity.ConcurrentUser;
import org.example.account.managment.entity.User;
import org.example.account.managment.service.exception.NotEnoughFundsException;

public interface ConcurrentUserService {

    boolean concurrentTransfer(ConcurrentUser sender, ConcurrentUser recipient, long amount) throws NotEnoughFundsException;

}
