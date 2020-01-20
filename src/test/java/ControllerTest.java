import org.example.account.managment.controller.Controller;
import org.example.account.managment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executors;

public class ControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    Controller controller;

    @BeforeEach
    public void initializeMocks(){
        MockitoAnnotations.initMocks(controller);
    }
}
