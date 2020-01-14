package org.example.account.managment;


import org.example.account.managment.entity.ConcurrentUser;
import org.example.account.managment.entity.User;
import org.example.account.managment.service.concurrent.ConcurrentUserService;
import org.example.account.managment.service.concurrent.ConcurrentUserServiceImpl;
import org.example.account.managment.service.exception.NotEnoughFundsException;

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
        Thread thread = new Thread(() -> {
            try {
                concurrentUserService.concurrentTransfer(concurrentUser, concurrentUser1, 60);
            } catch (NotEnoughFundsException e) {
                e.printStackTrace();
            }
        });
        Thread thread1 = new Thread(() -> {
            try {
                concurrentUserService.concurrentTransfer(concurrentUser1, concurrentUser, 150);
            } catch (NotEnoughFundsException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread1.start();
        try {
            thread.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(user.getAmount());
        System.out.println(user1.getAmount());
    }
}
