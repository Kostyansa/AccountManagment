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

public class UserTransferRequest implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(UserTransferRequest.class);

    private final UserService userService = Configuration.getUserService();

    private final Long user;

    private final String userId;

    private final String amount;

    public UserTransferRequest(Long userId, String userId2, String amount) {
        this.user = userId;
        this.userId = userId2;
        this.amount = amount;
    }

    /**
     * Parses inputs for recipient Id and amount of money for transfer
     * Then attempts transfer for user with {@link UserTransferRequest#user} id
     */
    @Override
    public void run() {

        Long idToTransfer;
        try {
            idToTransfer = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            logger.trace(String.format("Input for userId %s is not a valid id", userId));
            System.out.println("Recipient id is not valid");
            return;
        }

        long amountToTransfer;
        try{
            //Pattern that matches correct input for amount of money to transfer
            if (amount.matches("^\\d+[.]?\\d{0,2}$")) {
                amountToTransfer = Long.parseLong(amount.replaceAll("[.]", ""));
            }
            else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            logger.trace(String.format("Input for amount %s is not valid", amount));
            System.out.println("Amount to transfer is not valid");
            return;
        }

        try {
            User sender = userService.getUser(user);
            User recipient = userService.getUser(idToTransfer);
            userService.transfer(sender, recipient, amountToTransfer);
        } catch (UserNotFoundException e) {
            logger.trace(String.format("Transaction from %s failed because User with id %s has not been found", user, idToTransfer));
            System.out.println("Transaction failed recipient cannot be found");
            return;
        } catch (IllegalRecipientException e) {
            logger.trace(String.format("Transaction from %s failed because sender and recipient are the same", this.user));
            System.out.println("You cannot send transaction to yourself");
            return;
        } catch (NotEnoughFundsException e) {
            logger.trace(String.format("Transaction from %s failed because user don't have enough money", user));
            System.out.println("Balance is too low");
            return;
        }

        logger.trace("Transfer executed successfully");
    }
}
