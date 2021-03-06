import org.apache.commons.io.FileUtils;
import org.example.account.managment.configuration.Configuration;
import org.example.account.managment.controller.Controller;
import org.example.account.managment.controller.UserTransferRequest;
import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.concurrent.Executors;

public class ApplicationTest {

    private static final double emptyAccountCoefficient = 1.3;

    private static Logger logger = LoggerFactory.getLogger("TestLogger");

    public void runTest(int operationNumber, int threadNumber, UserRepository userRepository){
        List<User> users = userRepository.get();
        Long startingAmount = 0L;
        for (User user : users){
            startingAmount += user.getAmount();
            logger.info(user.toString());
        }
        logger.info(String.format("Total amount: %s.%s", startingAmount/100, startingAmount%100));
        Controller controller = new Controller(Executors.newFixedThreadPool(threadNumber));
        for (int i = 0; i < operationNumber; i++){
            controller.executeTask(new UserTransferRequest(
                    RandomGenerator.nextId(),
                    RandomGenerator.nextId().toString(),
                    RandomGenerator.nextAmount()
            ));
        }
        controller.shutdownAndAwaitTermination();
        users = userRepository.get();
        Long finalAmount = 0L;
        for (User user : users){
            finalAmount += user.getAmount();
            logger.info(user.toString());
        }
        logger.info(String.format("Total amount: %s.%s", finalAmount/100, finalAmount%100));
        Assertions.assertEquals(startingAmount, finalAmount);
        userRepository.saveCached();
    }

    public void InitializeUsers(int accountNumber) {
        new File(Configuration.pathToAccFiles).mkdirs();
        for (int i = 0; i < accountNumber; i++) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(Configuration.pathToAccFiles + i))) {
                User user = RandomGenerator.nextUser(i);
                outputStream.writeObject(user);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new ServiceConfigurationError("File could not be written to", e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceConfigurationError("IO Exception", e);
            }
        }
    }

    public void InitializeConfig(){
        Configuration.init();
    }

    @AfterEach
    public void deleteTestFolder(){
        try {
            FileUtils.deleteDirectory(new File(Configuration.pathToAccFiles));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @ParameterizedTest
    @ValueSource(ints = {1_000, 1_000_000, 0})
    public void requestsThroughputTest(int numberOfRequests) {
        int accountNumber = 2;
        RandomGenerator.ID_RANGE = (int) Math.floor(accountNumber*emptyAccountCoefficient);
        InitializeUsers(accountNumber);
        InitializeConfig();
        Assertions.assertTimeout(Duration.ofSeconds(60), () -> {
            runTest(numberOfRequests, 20, Configuration.getUserRepository());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 200, 1})
    public void threadTest(int threadNumber) {
        int accountNumber = 10;
        RandomGenerator.ID_RANGE = (int) Math.floor(accountNumber*emptyAccountCoefficient);
        InitializeUsers(accountNumber);
        InitializeConfig();
        Assertions.assertTimeout(Duration.ofSeconds(60), () -> {
            runTest(10_000, threadNumber, Configuration.getUserRepository());
        });
    }

    @Test
    public void zeroThreadsTest(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {runTest(1000, 0, Configuration.getUserRepository());}
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {50, 500})
    public void userAccountTest(int accountNumber){
        RandomGenerator.ID_RANGE = (int) Math.floor(accountNumber*emptyAccountCoefficient);
        InitializeUsers(accountNumber);
        InitializeConfig();
        Assertions.assertTimeout(Duration.ofSeconds(60), () -> {
            runTest(10000, 20, Configuration.getUserRepository());
        });
    }

    @Test
    public void zeroUsersTest(){
        RandomGenerator.ID_RANGE = 10;
        InitializeUsers(0);
        InitializeConfig();
        Assertions.assertTimeout(Duration.ofSeconds(1), () -> {
            runTest(10000, 20, Configuration.getUserRepository());
        });
    }

}
