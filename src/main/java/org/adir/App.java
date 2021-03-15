package org.adir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.adir.controller.CoffeeMachineController;
import org.adir.exception.CoffeeMachineException;
import org.adir.exception.InvalidRequestException;
import org.adir.model.Beverage;
import org.adir.model.Input;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application main class to execute basic flow of simultaneous beverage processing</pre>
 * 1. Get instance of controller
 * 2. Read Input
 * 3. Initialize coffee machine for service implementation, model and refill indicator
 * 4. Submit tasks to executor service to execute in parallel.
 */
public class App 
{
    public static void main( String[] args ) {
        CoffeeMachineController coffeeMachineController = CoffeeMachineController.INSTANCE;


        ObjectMapper objectMapper = new ObjectMapper();
        Input request = null;
        try {
            request = objectMapper.readValue(new File("src/main/resources/input.json"), Input.class);
        } catch (IOException e) {
            throw new InvalidRequestException("Failed to deserialise input request. Please rectify the json payload");
        }

        coffeeMachineController.initializeCoffeeMachine(request);

        Integer outlets = request.getMachine().getOutlets().getCount_n();
        ExecutorService executorService = Executors.newFixedThreadPool(outlets);
        for(Map.Entry<String,Map<String,Integer>> entry : request.getMachine().getBeverages().entrySet()){
            Beverage beverage = new Beverage(entry.getKey(),entry.getValue());
            executorService.submit(() -> {
                try {
                    coffeeMachineController.process(beverage);
                } catch (InterruptedException e) {
                    throw new CoffeeMachineException("Process interrupted while execution " + e.getMessage());
                }
            });
        }
        executorService.shutdown();
    }
}
