package it.astromark.commons.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SendGridMailService {
    private final SendGrid sendGrid;

    public SendGridMailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendAccessMail(String schoolCode, String username, String email, String password) {
        var from = new Email("noreply@astromark.it");
        var subject = "Credenziali Accesso AstroMark";
        var to = new Email(email);
        Content content = new Content("text/html", "<b> La tua password temporanea è: </b> <br> <br>" + password +
                "<br><br>Per favore, cambiala al primo accesso!" + "<br><br>" + "Il tuo username è: " + username + "<br>" + "Il tuo codice scuola è: " + schoolCode + "<p> <br><a href='https://astromark.it/login'>Clicca qui per accedere</a>" + "<br><br> " + "AstroMark Team </p>");
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            log.warn("Error sending the email", ex);
        }
    }
}
