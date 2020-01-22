package org.example.account.managment.service.concurrent;

import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.entity.User;
import org.example.account.managment.exception.IllegalRecipientException;
import org.example.account.managment.service.UserService;
import org.example.account.managment.exception.NotEnoughFundsException;
import org.example.account.managment.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

/**
 * Wrapper for {@link UserServiceImpl} that enables {@linkplain UserServiceImpl#transfer}
 * to be executed concurrently
 */
public class ConcurrentUserServiceWrapper extends UserServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(ConcurrentUserServiceWrapper.class);

    private void lockUsers(User sender, User recipient) {
        Lock firstLock;
        Lock secondLock;
        //Using global ordering for deadlock resolving
        if (sender.getId() < recipient.getId()) {
            firstLock = sender.getLock();
            secondLock = recipient.getLock();
        } else {
            firstLock = recipient.getLock();
            secondLock = sender.getLock();
        }
        logger.trace(String.format("Tried to lock: %s", firstLock.toString()));
        firstLock.lock();
        logger.trace(String.format("Tried to lock: %s", secondLock.toString()));
        secondLock.lock();
    }

    private void unlockUsers(User sender, User recipient) {
        Lock firstLock = sender.getLock();
        Lock secondLock = recipient.getLock();
        logger.trace(String.format("Tried to unlock: %s", secondLock.toString()));
        secondLock.unlock();
        logger.trace(String.format("Tried to unlock: %s", firstLock.toString()));
        firstLock.unlock();
    }


    @Override
    public boolean transfer(User sender, User recipient, long amount) throws NotEnoughFundsException, IllegalRecipientException {
        return this.concurrentTransfer(recipient, sender, amount);
    }

    private boolean concurrentTransfer(User sender, User recipient, long amount) throws NotEnoughFundsException, IllegalRecipientException {
        try {
            lockUsers(sender, recipient);
            return super.transfer(sender, recipient, amount);
        } finally {
            unlockUsers(sender, recipient);
        }
    }
}
