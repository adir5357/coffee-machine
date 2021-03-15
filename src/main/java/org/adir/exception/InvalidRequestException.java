package org.adir.exception;

public class InvalidRequestException extends CoffeeMachineException{
    public InvalidRequestException(String msg){
        super(msg);
    }
}
