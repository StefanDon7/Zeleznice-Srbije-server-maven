/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.stefanlezaic.zeleznice.srbije.server.email.sender;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import rs.stefanlezaic.zeleznice.srbije.lib.domen.Polazak;

/**
 *
 * @author Stefan
 */
public class Mail {

    private static String USER_NAME = "srbijezeleznice";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "lcvvdaoiswrcxidh"; // GMail password

    public void sendRegistrationMail(String email) {
        String[] to = {email};
        String subject = "ŽELEZNICE SRBIJE";
        String body
                = "<html>\n"
                + "<div class=\"div\">\n"
                + "    <h1>Železnice Srbije</h1>\n"
                + "    \n"
                + "    <p>Uspšeno ste se registrovali!</p>\n"
                + "     <p>Hvala na poverenju!</p>"
                + "</div>    \n"
                + "</html>";
        sendFromGMail(to, subject, body);
    }

    public void sendMail(String[] emails, Polazak p) {
        String[] to = emails;
        String subject = "ŽELEZNICE SRBIJE";
        String body
                = "<html>\n"
                + "<div class=\"div\">\n"
                + "    <h1>Železnice Srbije</h1>\n"
                + "    \n"
                + "     <p>Obaveštenje vezano za polazak:</p>\n"
                + "     <p>" + p.getNaziv() + "</p>\n"
                + "<p>\n</p>"
                + "<p>\n</p>"
                + "     <p>" + p.getNapomena() + "</p>\n"
                + "</div>    \n"
                + "</html>";

        sendFromGMail(to, subject, body);
    }

    private static void sendFromGMail(String[] to, String subject, String body) {
        String from = USER_NAME;
        String pass = PASSWORD;
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {

            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (InternetAddress toAddres : toAddress) {
                message.addRecipient(Message.RecipientType.TO, toAddres);
            }

            message.setSubject(subject);
            message.setText(body);
            message.setContent(body, "text/html; charset=utf-8");

            Transport transport = session.getTransport("smtp");

            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
