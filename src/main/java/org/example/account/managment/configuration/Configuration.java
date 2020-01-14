package org.example.account.managment.configuration;

import org.example.account.managment.service.UserService;
import org.example.account.managment.service.UserServiceImpl;

public class Configuration {
    public static UserService getUserService(){
        return new UserServiceImpl();
    }
}
