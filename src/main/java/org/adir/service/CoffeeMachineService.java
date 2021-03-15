package org.adir.service;

import org.adir.model.Beverage;

/**
 * CoffeeMachine interface to execute processing and refill of beverage
 */
public interface CoffeeMachineService {
    public void serve(Beverage beverage);

    public void refill(String ingredient);
}
