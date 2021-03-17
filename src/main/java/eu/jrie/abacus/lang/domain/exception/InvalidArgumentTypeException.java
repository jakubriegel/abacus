package eu.jrie.abacus.lang.domain.exception;

public class InvalidArgumentTypeException extends InvalidInputException {
    public InvalidArgumentTypeException(Exception cause) {
        super("InvalidArgumentTypeException", cause);
    }
}
