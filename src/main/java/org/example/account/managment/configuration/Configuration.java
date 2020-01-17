package org.example.account.managment.configuration;

import org.example.account.managment.service.UserService;
import org.example.account.managment.service.UserServiceImpl;

public class Configuration {

    private static final UserService userService = new UserServiceImpl();

    private static final String path = "";

    public static UserService getUserService(){
        return userService;
    }

}
