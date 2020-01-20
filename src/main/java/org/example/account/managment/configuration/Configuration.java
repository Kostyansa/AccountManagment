package org.example.account.managment.configuration;

import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.dao.UserRepositoryImpl;
import org.example.account.managment.entity.User;
import org.example.account.managment.service.UserService;
import org.example.account.managment.service.concurrent.ConcurrentUserServiceWrapper;
import org.example.account.managment.utils.RandomGenerator;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceConfigurationError;

public class Configuration {

    //Number of threads in a thread pool
    public static final int threadNumber = 20;

    //Number of requests to execute
    public static final int operationNumber = 1000;

    //Number of account files that will be created
    public static final int accountNumber = 10;

    //Relative path to folder with user accounts
    public static final String path = "./temp/";

    //User service bean
    private static UserService concurrentUserService;

    //User repository bean
    private static UserRepository userRepository;

    /**
     * Creates {@link Configuration#accountNumber} accounts in {@link Configuration#path} folder
     */
    public static void createUsers() {
        for (int i = 0; i < Configuration.accountNumber; i++) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(Configuration.path + i))) {
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

    public static void createUsers(int accountNumber) {
        for (int i = 0; i < accountNumber; i++) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(Configuration.path + i))) {
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

    /*
     * Initializes beans
     */

    public static void init() {
        try {
            userRepository = new UserRepositoryImpl(path, accountNumber);
        } catch (FileNotFoundException e) {
            throw new ServiceConfigurationError("File with user could not be found");
        } catch (IOException | ClassNotFoundException e) {
            throw new ServiceConfigurationError(e.getMessage(), e);
        }
        concurrentUserService = new ConcurrentUserServiceWrapper();
    }


    public static UserService getUserService() {
        return concurrentUserService;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }

}
