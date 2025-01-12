package it.astromark.commons.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridMailService {
    SendGrid sendGrid;

    public SendGridMailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public String sendAccessMail(String email) {
        var from = new Email("noreply@astromark.it");
        var subject = "Credenziali primo accesso AstroMark";
        var to = new Email(email);
        var password = new Faker().internet().password(8, 256, true, true, true);
        Content content = new Content("text/plain", "La tua password temporanea è: " + password);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            return null;
        }
        return password;
    }
}
