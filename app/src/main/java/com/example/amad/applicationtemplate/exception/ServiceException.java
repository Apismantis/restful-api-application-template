package com.example.amad.applicationtemplate.exception;

/**
 * Created by btloc on 12/2/16.
 */

public class ServiceException extends AMADException {

    private int code;

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
