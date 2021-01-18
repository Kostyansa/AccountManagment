import org.example.account.managment.dao.UserRepository;
import org.example.account.managment.entity.User;
import org.example.account.managment.exception.IllegalRecipientException;
import org.example.account.managment.exception.NotEnoughFundsException;
import org.example.account.managment.exception.UserNotFoundException;
import org.example.account.managment.service.UserService;
import org.example.account.managment.service.UserServiceImpl;
import org.example.account.managment.service.concurrent.ConcurrentUserServiceWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void SuccessfulUserServiceTest(){
        User user = new User(0L, 10000L, "", "");
        User user1 = new User(1L, 10000L, "", "");
        try {
            userService.transfer(user, user1, 5000);
        } catch (IllegalRecipientException | NotEnoughFundsException e) {
            fail("There was an Exception", e);
        }
        Assertions.assertEquals(5000, user.getAmount());
        Assertions.assertEquals(15000, user1.getAmount());
    }
}
