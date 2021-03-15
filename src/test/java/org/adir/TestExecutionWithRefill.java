package org.adir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.adir.controller.CoffeeMachineController;
import org.adir.model.Beverage;
import org.adir.model.Input;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestExecutionWithRefill {

    /**
     * <pre>Test runner to execute basic flow along with refill.</pre>
     * 1. Get instance of controller
     * 2. Read Input
     * 3. Initialize coffee machine for service implementation, model and refill indicator
     * 4. Submit tasks to executor service to execute in parallel.
     * 5. Get ingredients that match our refill criteria
     * 6. Refill ingredients
     * @throws IOException
     */
    @Test
    public void testExecutionWithRefill() throws IOException, InterruptedException {
        CoffeeMachineController coffeeMachineController = CoffeeMachineController.INSTANCE;

        ObjectMapper objectMapper = new ObjectMapper();
        Input request = objectMapper.readValue(new File("src/main/resources/input.json"), Input.class);

        coffeeMachineController.initializeCoffeeMachine(request);

        Integer outlets = request.getMachine().getOutlets().getCount_n();
        ExecutorService executorService = Executors.newFixedThreadPool(outlets);
        for(Map.Entry<String,Map<String,Integer>> entry : request.getMachine().getBeverages().entrySet()){
            Beverage beverage = new Beverage(entry.getKey(),entry.getValue());
            Future<String> successfulBeverages = (Future<String>) executorService.submit(() -> {
                try {
                    coffeeMachineController.process(beverage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        Thread.sleep(2000);

        List<String> refillEligibleIngredients = coffeeMachineController.getRefillScheduler().evaluateRefill();
        for(String ingredient : refillEligibleIngredients){
            coffeeMachineController.getCoffeeMachineService().refill(ingredient);
        }
    }
}
