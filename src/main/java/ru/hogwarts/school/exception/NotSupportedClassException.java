package ru.hogwarts.school.exception;

public class NotSupportedClassException extends RuntimeException {
    public NotSupportedClassException() {
        super();
    }

    public NotSupportedClassException(String message) {
        super(message);
    }

    public NotSupportedClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedClassException(Throwable cause) {
        super(cause);
    }

    protected NotSupportedClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
