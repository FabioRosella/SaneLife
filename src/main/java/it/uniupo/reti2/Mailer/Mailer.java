package it.uniupo.reti2.Mailer;
import java.util.Properties;

import javax.mail.*;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {
        Properties emailProperties;
        Session mailSession;
        MimeMessage emailMessage;

       /*public static void main(String args[]) throws AddressException,
                MessagingException {

            Mailer javaEmail = new Mailer();

            javaEmail.setMailServerProperties();
            javaEmail.createEmailMessage();
            javaEmail.sendEmail();
        }*/

        public void setMailServerProperties() {

            String emailPort = "587";//gmail's smtp port

            emailProperties = System.getProperties();
            emailProperties.put("mail.smtp.port", emailPort);
            emailProperties.put("mail.smtp.auth", "true");
            emailProperties.put("mail.smtp.starttls.enable", "true");

        }

        public void createEmailMessage() throws AddressException,
                MessagingException {
            String[] toEmails = { "daniel.colaianni96@gmail.com" };
            String emailSubject = "Paziente con battiti elevati";
            String emailBody = "La presente per avvertirla che il suo paziente non si sente bene e avviata cromoterapia.";

            mailSession = Session.getDefaultInstance(emailProperties);
            emailMessage = new MimeMessage(mailSession);

            for (int i = 0; i < toEmails.length; i++) {
                emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
            }

            emailMessage.setSubject(emailSubject);
            emailMessage.setContent(emailBody, "text/html");

        }

        public void sendEmail() throws AddressException, MessagingException {

            String emailHost = "smtp.gmail.com";
            String fromUser = "20013844@studenti.uniupo.it";
            String fromUserEmailPassword = "dani1996";

            Transport transport = mailSession.getTransport("smtp");

            transport.connect(emailHost, fromUser, fromUserEmailPassword);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            System.out.println("Inviata mail al medico");
        }


}
