package com.enviosya.cadete.excepcion;

public class DatoIncorrectoException extends Exception {

    public DatoIncorrectoException() {
        super();
    }

    public DatoIncorrectoException(String message) {
        super(message);
    }

    public DatoIncorrectoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatoIncorrectoException(Throwable cause) {
        super(cause);
    }
}
