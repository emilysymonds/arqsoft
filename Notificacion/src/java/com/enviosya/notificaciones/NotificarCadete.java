package com.enviosya.notificaciones;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName
            = "destinationLookup", propertyValue = "jms/QueueCadete"),
    @ActivationConfigProperty(propertyName
            = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName
            = "destinationType", propertyValue = "javax.jms.Queue")
    })

public class NotificarCadete implements MessageListener {

    @EJB
    Mail mailBean;
    
    Gson gson = new Gson();
    
    public NotificarCadete() {
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("Mensaje:");
        try {
            TextMessage msg = (TextMessage) message;
            String datos = msg.getText();
            System.out.println("Datos:" + datos);
            notificarCadete(datos);
        } catch (JMSException ex) {
            Logger.getLogger(NotificarCadete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void notificarCadete(String datos) {
        EnvioMensaje envio = gson.fromJson(datos, EnvioMensaje.class);
        String mensaje = envio.getDestinatario() + "-" + envio.getAsunto() + "-" + envio.getMensaje();
        mailBean.enviarMail(mensaje);
    }
}
