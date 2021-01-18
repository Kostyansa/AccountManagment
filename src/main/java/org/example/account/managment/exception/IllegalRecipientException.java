package org.example.account.managment.exception;

public class IllegalRecipientException extends Exception {
    public IllegalRecipientException() {
    }

    public IllegalRecipientException(String message) {
        super(message);
    }

    public IllegalRecipientException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRecipientException(Throwable cause) {
        super(cause);
    }
}
