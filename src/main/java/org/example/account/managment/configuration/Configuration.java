package org.example.account.managment.configuration;

import org.example.account.managment.service.UserService;
import org.example.account.managment.service.UserServiceImpl;

public class Configuration {

    private static final UserService userService = new UserServiceImpl();

    public static UserService getUserService(){
        return userService;
    }
}
