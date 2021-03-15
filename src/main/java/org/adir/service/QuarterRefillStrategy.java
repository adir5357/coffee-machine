package org.adir.service;

import org.adir.model.CoffeeMachine;
import org.adir.service.RefillStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Refill strategy that marks an ingredient to be eligible for refill if it reaches below 25% of its total capacity
 */
public class QuarterRefillStrategy implements RefillStrategy {
    @Override
    public List<String> evaluate(CoffeeMachine coffeeMachine) {
        List<String> refillEligibleIngredients = new ArrayList<>();
        Map<String,Integer> supportedIngredientsToCapacity = coffeeMachine.getCapacityForIngredient();
        for(Map.Entry<String,Integer> entry : supportedIngredientsToCapacity.entrySet()){
            Integer totalCapacity = entry.getValue();
            Integer availablility = coffeeMachine.getAvailableIngredientQuantity(entry.getKey());
            if(availablility <= totalCapacity/4){
                refillEligibleIngredients.add(entry.getKey());
            }
        }
        return refillEligibleIngredients;
    }
}
