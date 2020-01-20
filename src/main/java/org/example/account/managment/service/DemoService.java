package org.example.account.managment.service;

import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.controller.Controller;
import org.example.account.managment.controller.UserTransferRequest;
import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.entity.User;
import org.example.account.managment.utils.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    public void runDemo(){
        Configuration.createUsers();
        Configuration.init();
        UserRepository userRepository = Configuration.getUserRepository();
        List<User> users = userRepository.get();
        Long startingAmount = 0L;
        for (User user : users){
            startingAmount += user.getAmount();
            System.out.println(user.toString());
        }
        System.out.println(String.format("Total amount: %s.%s", startingAmount/100, startingAmount%100));
        Controller controller = new Controller();
        for (int i = 0; i < Configuration.operationNumber; i++){
            controller.ExecuteTask(new UserTransferRequest(
                    RandomGenerator.nextId(),
                    RandomGenerator.nextId().toString(),
                    RandomGenerator.nextAmount()
            ));
            logger.debug(String.format("Sent task %s", i));
        }
        controller.shutdownAndAwaitTermination();

        users = userRepository.get();
        Long finalAmount = 0L;
        for (User user : users){
            finalAmount += user.getAmount();
            System.out.println(user.toString());
        }
        System.out.println(String.format("Total amount: %s.%s", finalAmount/100, finalAmount%100));
        System.out.println("Is final amount equals starting amount?");
        System.out.println(startingAmount.equals(finalAmount) ? "yes" : "no");
        userRepository.saveCached();
    }
}
