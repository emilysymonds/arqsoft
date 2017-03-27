package com.enviosya.envios.excepcion;

public class ErrorUrlException extends Exception {

    public ErrorUrlException() {
        super();
    }

    public ErrorUrlException(String message) {
        super(message);
    }

    public ErrorUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorUrlException(Throwable cause) {
        super(cause);
    }
}
