package org.example.account.managment.service.concurrent;

import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.entity.ConcurrentUser;
import org.example.account.managment.service.UserService;
import org.example.account.managment.service.exception.NotEnoughFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

public class ConcurrentUserServiceImpl implements ConcurrentUserService {
    Logger logger = LoggerFactory.getLogger(ConcurrentUserService.class);

    UserService userService = Configuration.getUserService();

    private void lockUsers(ConcurrentUser sender, ConcurrentUser recipient) {
        Lock firstLock;
        Lock secondLock;
        if (sender.getUser().getId() < recipient.getUser().getId()){
            firstLock = sender.getLock();
            secondLock = recipient.getLock();
        }
        else{
            firstLock = recipient.getLock();
            secondLock = sender.getLock();
        }
        logger.trace(String.format("Tried to lock: %s", firstLock.toString()));
        firstLock.lock();
        logger.trace(String.format("Tried to lock: %s", secondLock.toString()));
        secondLock.lock();
    }

    private void unlockUsers(ConcurrentUser sender, ConcurrentUser recipient) {
        Lock firstLock = sender.getLock();
        Lock secondLock = recipient.getLock();
        logger.trace(String.format("Tried to unlock: %s", secondLock.toString()));
        secondLock.unlock();
        logger.trace(String.format("Tried to unlock: %s", firstLock.toString()));
        firstLock.unlock();
    }

    public boolean concurrentTransfer(ConcurrentUser sender, ConcurrentUser recipient, long amount) throws NotEnoughFundsException {
        lockUsers(sender, recipient);
        try {
            return userService.transfer(sender.getUser(), recipient.getUser(), amount);
        } finally {
            unlockUsers(sender, recipient);
        }
    }
}
