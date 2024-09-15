package is.yarr.qilletni.lib.http.exceptions;

import is.yarr.qilletni.api.exceptions.QilletniException;

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
