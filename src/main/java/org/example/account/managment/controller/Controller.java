package org.example.account.managment.controller;

import org.example.account.managment.configuration.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller {

    ExecutorService executorService = Executors.newFixedThreadPool(Configuration.threadNumber);

    public void ExecuteTask(UserTransferRequest request){
        executorService.submit(request);
    }

    public void shutdown(){
        executorService.shutdown();
    }

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
