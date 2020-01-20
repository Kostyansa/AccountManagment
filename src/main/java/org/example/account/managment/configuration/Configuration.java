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

    public static final int threadNumber = 20;

    public static final int operationNumber = 1000;

    public static final int accountNumber = 10;

    public static final String path = "./temp/";

    private static UserService concurrentUserService;

    private static UserRepository userRepository;

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
