package org.example.account.managment.configuration;

import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.dao.UserRepositoryImpl;
import org.example.account.managment.service.UserService;
import org.example.account.managment.service.concurrent.ConcurrentUserServiceWrapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ServiceConfigurationError;

public class Configuration {

    public static final int threadNumber = 20;

    public static final int operationNumber = 1000;

    public static final String path = "";

    private static final UserService concurrentUserService;

    private static final UserRepository userRepository;

    static {
        try {
            concurrentUserService = new ConcurrentUserServiceWrapper();
            userRepository = new UserRepositoryImpl(new URI(path));
        } catch (URISyntaxException e) {
            throw new ServiceConfigurationError("User Repository has not been initialized", e);
        }
    };


    public static UserService getUserService(){
        return concurrentUserService;
    }

    public static UserRepository getUserRepository(){
        return userRepository;
    }

}
