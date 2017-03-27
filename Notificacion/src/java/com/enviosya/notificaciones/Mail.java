package com.enviosya.notificaciones;

import javax.mail.PasswordAuthentication;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class Mail {

    public void enviarMail(String msg) {

        String[] datos = msg.split("-");
        String to = datos[0];
        String subject = datos[1];
        String body = datos[2];

        final String from = "enviosya.notificaciones@gmail.com";
        final String username = "enviosya.notificaciones";
        final String password = "obligatorio2016";
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        Session session;
        session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("¡Mensaje enviado correctamente!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
}
