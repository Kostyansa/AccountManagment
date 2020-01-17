package org.example.account.managment;


import org.example.account.managment.entity.ConcurrentUser;
import org.example.account.managment.entity.User;
import org.example.account.managment.service.concurrent.ConcurrentUserService;
import org.example.account.managment.service.concurrent.ConcurrentUserServiceImpl;
import org.example.account.managment.exception.NotEnoughFundsException;

import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        User user = new User();
        user.setAmount(100.00);
        user.setId(1L);
        User user1 = new User();
        user1.setAmount(100.00);
        user1.setId(2L);
        ConcurrentUser concurrentUser = new ConcurrentUser(user);
        ConcurrentUser concurrentUser1 = new ConcurrentUser(user1);
        ConcurrentUserService concurrentUserService = new ConcurrentUserServiceImpl();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            threads.add(new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        concurrentUserService.concurrentTransfer(concurrentUser, concurrentUser1, 10);
                    } catch (NotEnoughFundsException e) {
                        System.out.println("Not enough funds");
                    }
                }
            }));
            threads.add(new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        concurrentUserService.concurrentTransfer(concurrentUser1, concurrentUser, 10);
                    } catch (NotEnoughFundsException e) {
                        System.out.println("Not enough funds");
                    }
                }
            }));
        }
        for(Thread thread : threads){
            thread.start();
        }
        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(user.getAmount());
        System.out.println(user1.getAmount());
    }
}
