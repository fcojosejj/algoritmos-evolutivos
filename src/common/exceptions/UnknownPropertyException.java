package common.exceptions;

public class UnknownPropertyException extends Exception {
    public UnknownPropertyException() {
    }

    public UnknownPropertyException(String message) {
        super(message);
    }
}

