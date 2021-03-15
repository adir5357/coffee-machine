package org.adir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.adir.controller.CoffeeMachineController;
import org.adir.model.Beverage;
import org.adir.model.Input;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BaseTestRunner
{
    /**
     * <pre>Test runner to execute basic flow provided in the input json where different beverages are ordered simultaneously.</pre>
     * 1. Get instance of controller
     * 2. Read Input
     * 3. Initialize coffee machine for service implementation, model and refill indicator
     * 4. Submit tasks to executor service to execute in parallel.
     * @throws IOException
     */
    @Test
    public void testExecution() throws IOException {
        CoffeeMachineController coffeeMachineController = CoffeeMachineController.INSTANCE;

        ObjectMapper objectMapper = new ObjectMapper();
        Input request = objectMapper.readValue(new File("src/main/resources/input.json"), Input.class);

        coffeeMachineController.initializeCoffeeMachine(request);

        Integer outlets = request.getMachine().getOutlets().getCount_n();
        ExecutorService executorService = Executors.newFixedThreadPool(outlets);
        for(Map.Entry<String,Map<String,Integer>> entry : request.getMachine().getBeverages().entrySet()){
            Beverage beverage = new Beverage(entry.getKey(),entry.getValue());
            executorService.submit(() -> {
                try {
                    coffeeMachineController.process(beverage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }
}
