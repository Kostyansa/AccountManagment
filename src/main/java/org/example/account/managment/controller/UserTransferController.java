package org.example.account.managment.controller;

import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.entity.User;
import org.example.account.managment.exception.IllegalRecipientException;
import org.example.account.managment.exception.NotEnoughFundsException;
import org.example.account.managment.exception.UserNotFoundException;
import org.example.account.managment.service.UserService;
import org.example.account.managment.utils.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class UserTransferController implements Runnable {

    Logger logger = LoggerFactory.getLogger(UserTransferController.class);

    private final UserService userService = Configuration.getUserService();

    private final User user;

    public static final AtomicInteger counter = new AtomicInteger();

    public UserTransferController(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        while (counter.getAndIncrement() < Configuration.operationNumber){
            Long idToTransfer = RandomGenerator.nextId();
            double amountToTransfer = RandomGenerator.nextAmount();
            try{
                User recipient = userService.getUser(idToTransfer);
                userService.transfer(this.user, recipient, amountToTransfer);
            } catch (UserNotFoundException e) {
                logger.trace(String.format("Transaction from %s failed because User with id %s has not been found", this.user, idToTransfer), e);
                System.out.println("Transaction failed recipient cannot be found");
            } catch (IllegalRecipientException e) {
                logger.trace(String.format("Transaction from %s failed because sender and recipient are the same", this.user), e);
                System.out.println("You cannot send transaction to yourself");
            } catch (NotEnoughFundsException e) {
                logger.trace(String.format("Transaction from %s failed because user don't have enough money: %s", this.user, this.user.getAmount()), e);
                System.out.println("Balance is too low");
            }
        }
    }
}
