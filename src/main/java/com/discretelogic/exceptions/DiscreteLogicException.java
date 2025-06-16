package com.discretelogic.exceptions;

public abstract class DiscreteLogicException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public DiscreteLogicException(String message) {
        super(message);
    }

    public DiscreteLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}