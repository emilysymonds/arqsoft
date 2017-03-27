package com.enviosya.envios.excepcion;

public class EntidadNoExisteException extends Exception {

    public EntidadNoExisteException() {
        super();
    }

    public EntidadNoExisteException(String message) {
        super(message);
    }

    public EntidadNoExisteException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntidadNoExisteException(Throwable cause) {
        super(cause);
    }
}
