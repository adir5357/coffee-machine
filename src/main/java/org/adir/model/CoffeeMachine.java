package org.adir.model;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Data
/**
 * coffeeMachine entity class with total no of outlets, capacity per ingredient and current availability per ingredient
 */
public class CoffeeMachine {

    private final static Logger LOG = LoggerFactory.getLogger(CoffeeMachine.class);

    private Integer outlets;
    private Map<String,Integer> capacityForIngredient;
    private ConcurrentHashMap<String,Integer> availabilityForIngredient;

    public CoffeeMachine(Integer outlets, Map<String, Integer> capacityForIngredient) {
        this.outlets = outlets;
        this.capacityForIngredient = capacityForIngredient;
        availabilityForIngredient = new ConcurrentHashMap<>();
        for(Map.Entry<String,Integer> entry : capacityForIngredient.entrySet()){
            availabilityForIngredient.put(entry.getKey(),entry.getValue());
        }
    }

    public Integer getAvailableIngredientQuantity(String ingredientName){
        return availabilityForIngredient.get(ingredientName);
    }

    public void updateIngredientAvailability(String ingredientName, Integer updatedQuantity){
        availabilityForIngredient.put(ingredientName, updatedQuantity);
    }

    public void refill(String ingredientName){
        availabilityForIngredient.put(ingredientName, capacityForIngredient.get(ingredientName));
        System.out.println("refilled: "+ingredientName+" to capacity: "+capacityForIngredient.get(ingredientName));
    }
}
