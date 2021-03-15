package org.adir.exception;

/**
 * Generic service exception to encapsulate internal exceptions
 */
public class CoffeeMachineException extends RuntimeException {
    public CoffeeMachineException(String msg){
        super(msg);
    }
}
