package org.example.account.managment.controller;

import org.example.account.managment.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    ExecutorService executorService;

    public Controller(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void ExecuteTask(UserTransferRequest request){
        executorService.submit(request);
    }

    /**
     * Stops {@link Controller#executorService} from executing new tasks
     * and waits 60 seconds before terminating existing tasks
     */
    public void shutdownAndAwaitTermination() {
        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("executorService did not terminate");
            }
        } catch (InterruptedException ie) {
            // Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
