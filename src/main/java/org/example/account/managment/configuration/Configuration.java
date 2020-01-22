package org.example.account.managment.configuration;

import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.dao.UserRepositoryImpl;
import org.example.account.managment.service.UserService;
import org.example.account.managment.service.concurrent.ConcurrentUserServiceWrapper;

import java.io.*;
import java.util.ServiceConfigurationError;

public class Configuration {

    //Relative path to folder with user accounts
    public static final String pathToAccFiles = "./temp/";

    //User service bean
    private static UserService concurrentUserService;

    //User repository bean
    private static UserRepository userRepository;

    /*
     * Initializes beans
     */
    public static void init() {
        try {
            userRepository = new UserRepositoryImpl(pathToAccFiles);
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
