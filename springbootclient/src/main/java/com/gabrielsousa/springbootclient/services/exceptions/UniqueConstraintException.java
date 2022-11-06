package com.gabrielsousa.springbootclient.services.exceptions;

public class UniqueConstraintException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UniqueConstraintException(String msg) {
        super(msg);
    }
}
