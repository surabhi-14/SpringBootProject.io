package com.wu.service;



import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	public boolean sendEmail(String subject, String message, String to){

        boolean f= false;

        String from = "wcollege445@gmail.com";

        // variable for gmail

        String host = "smtp.gmail.com";

        // get system properties
        Properties properties = System.getProperties();
        System.out.println(properties);
        // settings important info to properties object
        // host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // step 1 get session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("wcollege445@gmail.com","College445@");
            }
        });
        session.setDebug(true);
        // step2 compose message
        MimeMessage msg = new MimeMessage(session);

        try {
            // from email
            msg.setFrom(from);
            //adding recipient
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //adding subject to msg
            msg.setSubject(subject);
            //adding message
            //msg.setText(message);
            msg.setContent(message,"text/html");

            //send

            //step 3 send the message using transport class
            Transport.send(msg);
            System.out.println("sent successfully");
            f=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
	
}
