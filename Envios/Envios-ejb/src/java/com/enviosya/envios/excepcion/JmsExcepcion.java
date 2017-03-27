package com.enviosya.envios.excepcion;

public class JmsExcepcion extends Exception {

    public JmsExcepcion() {
        super();
    }

    public JmsExcepcion(String message) {
        super(message);
    }

    public JmsExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public JmsExcepcion(Throwable cause) {
        super(cause);
    }
}
