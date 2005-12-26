package org.iterx.miru.dispatcher;

import java.io.IOException;

public class DispatcherException extends IOException {

    public DispatcherException() {}

    public DispatcherException(String message) {

        super(message);
    }

    public DispatcherException(Throwable cause) {

        initCause(cause);
    }

    public DispatcherException(String message, Throwable cause) {

        super(message);
        initCause(cause);
    }

}
