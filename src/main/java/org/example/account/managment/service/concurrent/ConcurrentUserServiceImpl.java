package org.example.account.managment.service.concurrent;

import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.entity.ConcurrentUser;
import org.example.account.managment.entity.User;
import org.example.account.managment.service.UserService;
import org.example.account.managment.service.exception.NotEnoughFundsException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentUserServiceImpl implements ConcurrentUserService {

    UserService userService = Configuration.getUserService();

    private ReentrantLock lockLock = new ReentrantLock();

    private void lockUsers(ConcurrentUser sender, ConcurrentUser recipient) {
        Lock senderWriteLock = sender.getWriteLock();
        Lock recipientWriteLock = recipient.getWriteLock();
        senderWriteLock.lock();
        recipientWriteLock.lock();
    }

    private void unlockUsers(ConcurrentUser sender, ConcurrentUser recipient) {
        Lock senderWriteLock = sender.getWriteLock();
        Lock recipientWriteLock = recipient.getWriteLock();
        senderWriteLock.unlock();
        recipientWriteLock.unlock();
    }

    public boolean concurrentTransfer(ConcurrentUser sender, ConcurrentUser recipient, long amount) throws NotEnoughFundsException {
        lockUsers(sender, recipient);
        try {
            userService.transfer(sender.getUser(), recipient.getUser(), amount);
        } finally {
            unlockUsers(sender, recipient);
        }
        return false;
    }
}
