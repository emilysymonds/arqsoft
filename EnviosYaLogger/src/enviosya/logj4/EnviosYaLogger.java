package enviosya.logj4;

import org.apache.log4j.Logger;


public class EnviosYaLogger {
    
    public Logger log;
    
    public EnviosYaLogger(Class nombre) {
        log = Logger.getLogger(nombre.getName());
    }
    
    public void debug(String texto) {
        log.debug(texto);
    }
    
    
}
