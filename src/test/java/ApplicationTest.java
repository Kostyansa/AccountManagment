import org.example.account.managment.controller.Controller;
import org.example.account.managment.controller.UserTransferRequest;
import org.example.account.managment.service.DemoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

public class ApplicationTest {

    @ParameterizedTest
    @ValueSource(ints = {1000, 1000000, 0})
    public void requestsTest(int numberOfRequests){
        Assertions.assertTimeout(Duration.ofSeconds(60), () -> {
            new DemoService().runDemo(numberOfRequests);
        });
    }
}
