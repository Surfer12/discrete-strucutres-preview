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

public final class MathematicalOperationException extends DiscreteLogicException {
    public MathematicalOperationException(String message) {
        super(message);
    }
}

public final class ParsingException extends DiscreteLogicException {
    public ParsingException(String message) {
        super(message);
    }
}

public final class ValidationException extends DiscreteLogicException {
    public ValidationException(String message) {
        super(message);
    }
}