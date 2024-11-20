package Dominio;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author wikit
 */
public class Correos {

    private Properties properties;

    private static String password = "sxwolabtfnggeomo";
    private static String emailForm = "tallersosman@gmail.com";

    private Session session;
    private MimeMessage message;

    public Correos() {
        properties = new Properties();
    }

    private void init() {

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");

        //Usuario para el correo
        properties.setProperty("mail.smtp.user", emailForm);

        session = Session.getDefaultInstance(properties);

    }

    public String sendEmail(String receptor, String texto) {
        init();
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailForm));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            message.setSubject("Recordatorio de mantenimiento");
            message.setText(texto, "ISO-8859-1", "html");

            //Envio
            Transport t = session.getTransport("smtp");
            t.connect(emailForm, password);
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            t.close();
            return texto;

        } catch (MessagingException me) {
            return null;
        }

    }
}
