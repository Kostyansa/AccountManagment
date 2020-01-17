package org.example.account.managment.utils;

import org.example.account.managment.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class RandomGenerator {

    private static final int ID_RANGE = 200;

    private static final int MAX_BALANCE_TO_TWO_DIGITS = 50000;

    private static final ReentrantLock randomLock = new ReentrantLock();

    private static final Random random = new Random();

    private static ArrayList<String> names = new ArrayList<>(Arrays.asList(
            "John Smith",
            "Petr Ivanov",
            "High Ping",
            "Sum Ting Wong",
            "Lenin",
            "Anna Frank",
            "Marie Curie",
            "Ivan Petrov"
    ));

    public static User nextUser(){
        User user = new User();
        randomLock.lock();
        try {
            user.setId((long) random.nextInt(ID_RANGE));
            user.setAmount(random.nextInt(MAX_BALANCE_TO_TWO_DIGITS)*0.01);
            user.setName(names.get(random.nextInt(names.size())));
            StringBuilder phoneNumber = new StringBuilder().append("+79");
            for (int i = 0; i < 9; i++){
                phoneNumber.append(random.nextInt(10));
            }
            user.setPhoneNumber(phoneNumber.toString());
        }
        finally {
            randomLock.unlock();
        }
        return user;
    }

    public static Long nextId(){
        long id = 0L;
        randomLock.lock();
        try{
            id = (long) random.nextInt(ID_RANGE);
        }
        finally {
            randomLock.unlock();
        }
        return id;
    }

    public static double nextAmount(){
        double amount = 0L;
        randomLock.lock();
        try{
            if (random.nextDouble() < 0.9) {
                amount = random.nextInt(MAX_BALANCE_TO_TWO_DIGITS) * 2 * 0.01;
            }
            else{
                amount = random.nextDouble()*MAX_BALANCE_TO_TWO_DIGITS * 2 * 0.01;
            }
        }
        finally {
            randomLock.unlock();
        }
        return amount;
    }
}
