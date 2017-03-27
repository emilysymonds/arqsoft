package com.enviosya.cadete.excepcion;

public class EntidadYaExisteException extends Exception {

    public EntidadYaExisteException() {
        super();
    }

    public EntidadYaExisteException(String message) {
        super(message);
    }

    public EntidadYaExisteException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntidadYaExisteException(Throwable cause) {
        super(cause);
    }
}
