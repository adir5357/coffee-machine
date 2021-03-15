package org.adir.service;

import org.adir.exception.InadequateIngredientException;
import org.adir.model.Beverage;
import org.adir.model.CoffeeMachine;

import java.text.MessageFormat;
import java.util.Map;

/**
 * CoffeeMachine service implementation class for processing and refill methods
 */
public class CoffeeMachineImpl implements CoffeeMachineService {

    private CoffeeMachine coffeeMachine;

    public CoffeeMachineImpl(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    /**
     * Process beverage based on required ingredients if availability is satisfied.
     * @param beverage
     */
    public void serve(Beverage beverage) {
        String beverageName = beverage.getName();
        //Critical secion
        //Check if ingredients satisfy the availability criteria for the beverage
        //If it's satisfied, beverage is served
        synchronized(this) {
            if(isBeverageEligibleToServe(beverage)) {
                prepareBeverage(beverage);
                System.out.println(beverageName + " is prepared");
            }
        }
    }

    /**
     * Process beverage and update ingredients availability for coffee machine
     * @param beverage
     */
    private void prepareBeverage(Beverage beverage) {
        for (Map.Entry<String, Integer> ingredient : beverage.getIngredientByQuantity().entrySet()) {
            String ingredientName = ingredient.getKey();
            Integer requiredQuantity = beverage.getIngredientByQuantity().get(ingredient.getKey());
            Integer availableQuantity = coffeeMachine.getAvailableIngredientQuantity(ingredientName);
            Integer updatedQuantity = availableQuantity - requiredQuantity;
            coffeeMachine.updateIngredientAvailability(ingredientName, updatedQuantity);
        }
    }

    /**
     * Check if all the ingredients required for beverage are available
     * @param beverage
     * @return boolean if all the ingredients required for beverage are available
     * @throws InadequateIngredientException if availability criteria is not met.
     */
    private boolean isBeverageEligibleToServe(Beverage beverage) {
        try {
            for (Map.Entry<String, Integer> ingredient : beverage.getIngredientByQuantity().entrySet()) {
                String ingredientName = ingredient.getKey();
                Integer requiredQuantity = beverage.getIngredientByQuantity().get(ingredient.getKey());
                Integer availableQuantity = coffeeMachine.getAvailableIngredientQuantity(ingredientName);
                if (availableQuantity == 0) {
                    String message = MessageFormat.format("{0} cannot be prepared because {1} is not available", beverage.getName(), ingredientName);
                    throw new InadequateIngredientException(message);
                }
                if (availableQuantity < requiredQuantity) {
                    String message = MessageFormat.format("{0} cannot be prepared because item {1} is not sufficient", beverage.getName(), ingredientName);
                    throw new InadequateIngredientException(message);
                }
            }
            return true;
        }catch (InadequateIngredientException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Refill ingredient in the coffeeMachine
     * @param ingredient
     */
    public void refill(String ingredient){
        coffeeMachine.refill(ingredient);
    }
}
