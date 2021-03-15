package org.adir.service;

import org.adir.model.CoffeeMachine;

import java.util.List;

/**
 * Interface for different strategies that can be used to evaluate if refill is required of not
 */
public interface RefillStrategy {
    public List<String> evaluate(CoffeeMachine coffeeMachine);
}
