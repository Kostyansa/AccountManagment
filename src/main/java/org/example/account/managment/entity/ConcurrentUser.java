package org.example.account.managment.entity;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Concurrent wrapper for {@link User}
 * Can be replaced with synchronized keyword
 */
public class ConcurrentUser {

    private User user;

    transient private final ReentrantLock lock = new ReentrantLock();

    public ConcurrentUser(User user) {
        this.user = user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public ReentrantLock getLock(){
        return lock;
    }
}
