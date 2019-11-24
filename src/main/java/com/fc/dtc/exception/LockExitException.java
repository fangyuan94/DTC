package com.fc.dtc.exception;

/**
 * lock存在移除
 * @author fangyuan
 */
public class LockExitException extends RuntimeException {

    public LockExitException() {
        super();
    }

    public LockExitException(String message, Throwable cause,
                              boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public LockExitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockExitException(String message) {
        super(message);
    }

    public LockExitException(Throwable cause) {
        super(cause);
    }

}
