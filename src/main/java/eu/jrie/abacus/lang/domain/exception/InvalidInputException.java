package eu.jrie.abacus.lang.domain.exception;

abstract public class InvalidInputException extends Exception {
    protected InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
