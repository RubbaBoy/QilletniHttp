package dev.qilletni.lib.http.exceptions;

import dev.qilletni.api.exceptions.QilletniException;

public class IllegalHeaderTypeException extends QilletniException {

    public IllegalHeaderTypeException() {
        super();
    }

    public IllegalHeaderTypeException(String message) {
        super(message);
    }

    public IllegalHeaderTypeException(Throwable cause) {
        super(cause);
    }
}
