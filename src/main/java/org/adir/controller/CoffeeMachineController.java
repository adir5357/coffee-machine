package org.adir.controller;

import lombok.Getter;
import org.adir.model.Beverage;
import org.adir.model.CoffeeMachine;
import org.adir.model.Input;
import org.adir.service.QuarterRefillStrategy;
import org.adir.scheduler.RefillScheduler;
import org.adir.service.CoffeeMachineImpl;
import org.adir.service.CoffeeMachineService;

@Getter
public enum CoffeeMachineController {

    INSTANCE;

    private CoffeeMachine coffeeMachine;
    private CoffeeMachineService coffeeMachineService;
    private RefillScheduler refillScheduler;

    public void initializeCoffeeMachine(Input request) {
        System.out.println("<========Initialized coffee machine=========>");
        Integer outlets = request.getMachine().getOutlets().getCount_n();
        coffeeMachine = new CoffeeMachine(outlets,request.getMachine().getTotal_items_quantity());
        coffeeMachineService = new CoffeeMachineImpl(coffeeMachine);
        refillScheduler = new RefillScheduler(new QuarterRefillStrategy(),coffeeMachine);
    }

    public void process(Beverage beverage) throws InterruptedException {
        coffeeMachineService.serve(beverage);
    }

}
