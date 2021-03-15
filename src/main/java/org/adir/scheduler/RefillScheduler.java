package org.adir.scheduler;

import org.adir.model.CoffeeMachine;
import org.adir.service.RefillStrategy;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Single Threaded scheduler to simulate refill indicator which sends notification with the list of ingredients for whom refill is required
 */
public class RefillScheduler {
    private RefillStrategy refillStrategy;
    private CoffeeMachine coffeeMachine;
    public RefillScheduler(RefillStrategy refillStrategy,CoffeeMachine coffeeMachine) {
        this.refillStrategy = refillStrategy;
        this.coffeeMachine = coffeeMachine;
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::evaluateRefill,2,10, TimeUnit.SECONDS);
    }

    public List<String> evaluateRefill(){
        List<String> ingredientsEligibleForRefill =  refillStrategy.evaluate(coffeeMachine);
        System.out.println("<======== Refill Indicator ==========>");
        System.out.println(ingredientsEligibleForRefill + " requires Refill");
        return ingredientsEligibleForRefill;
    }
}
