package org.example.account.managment.entity;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentUser {

    private User user;

    transient private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ConcurrentUser(User user) {
        this.user = user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public ReentrantReadWriteLock.ReadLock getReadLock(){
        return lock.readLock();
    }

    public ReentrantReadWriteLock.WriteLock getWriteLock(){
        return lock.writeLock();
    }

    public ReentrantReadWriteLock getLock(){
        return lock;
    }
}
