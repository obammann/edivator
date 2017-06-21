package com.gruppe4b.edivator.backend.service;

import com.google.api.server.spi.BackendService;
import com.google.appengine.api.images.Image;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public void sendImage(Image image, String recipient) {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("noreply@gruppe-4b.appspotmail.com", "Edivator"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient, "Mr. User"));
            msg.setSubject("Dein bearbeitetes Bild");

            String htmlBody = "Hallo, dein bearbeitetes Bild liegt im Anhang. \n Mfg. Edivator";
            byte[] attachmentData = image.getImageData();
            Multipart mp = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            InputStream attachmentDataStream = new ByteArrayInputStream(attachmentData);
            attachment.setFileName("DeinBild.jpg");
            attachment.setContent(attachmentDataStream, "image/jpeg");
            mp.addBodyPart(attachment);

            msg.setContent(mp);

            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
