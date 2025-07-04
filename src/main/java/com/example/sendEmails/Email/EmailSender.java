package com.example.sendEmails.Email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class EmailSender {

    private final EmailConfig config;

    public EmailSender(EmailConfig config) {
        this.config = config;
    }

    public ResponseEntity<String> sendTextEmail(List<String> to, String title, String body, Date date) {
        // define as confuguações
        Session session = Session.getInstance(config.toProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });

        session.setDebug(config.isDebug());

        try {
            // criaçãaoo da mensagem
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getUsername())); // email do remetente

            // adicionando destintários
            InternetAddress[] recipients = to.stream()  //lista de emails destinatários
                    .map(email -> {
                        try {
                            return new InternetAddress(email);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(InternetAddress[]::new);

            message.setRecipients(Message.RecipientType.TO, recipients); // "para: destinatarios..."
            message.setSubject(title, "UTF-8"); // titulo
            message.setText(body, "UTF-8"); // mensagem
            message.setSentDate(date); // data de envio

            Transport.send(message); // emvia
            return ResponseEntity.ok("✅ E-mail enviado com sucesso!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
