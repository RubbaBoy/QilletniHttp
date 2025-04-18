package dev.qilletni.lib.http.exceptions;

import dev.qilletni.api.exceptions.QilletniException;

public class UnableToSendHttpRequest extends QilletniException {

    public UnableToSendHttpRequest() {
        super();
    }

    public UnableToSendHttpRequest(String message) {
        super(message);
    }

    public UnableToSendHttpRequest(Throwable cause) {
        super(cause);
    }
}
