package iosr.faceboookapp.publisher.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by Leszek Placzkiewicz on 09.12.17.
 */
@Component
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);


    @Autowired
    public JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String text) {
        LOG.info("Sending email to: " + to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}
