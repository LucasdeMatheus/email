package com.myproject.sendEmails.email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EmailSender {

    private final EmailConfig config;

    public EmailSender(EmailConfig config) {
        this.config = config;
    }

    public ResponseEntity<String> sendTextEmail(List<String> to, Type type, Date date, Map<String, String> data) throws IOException {
        // define as confuguações
        Session session = Session.getInstance(config.toProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
        String title = "";
        String body = "";
        File input = null;
        switch (type){
            case WELLCOME ->{
                input = new File("C:/Users/ffgus/Desktop/sendEmails/src/main/resources/emails/welcome.compiled.html");
                body = Files.readString(Paths.get("C:/Users/ffgus/Desktop/sendEmails/src/main/resources/emails/welcome.compiled.html"));
            }
            case VALIDEMAIL -> {
                input = new File("C:/Users/ffgus/Desktop/sendEmails/src/main/resources/emails/confirm-email.compiled.html");
                body = Files.readString(Paths.get("C:/Users/ffgus/Desktop/sendEmails/src/main/resources/emails/confirm-email.compiled.html"));
            }
            case UPPASSWORD -> {
                input = new File("C:/Users/ffgus/Desktop/sendEmails/src/main/resources/emails/up-password.compiled.html");
                body = Files.readString(Paths.get("C:/Users/ffgus/Desktop/sendEmails/src/main/resources/emails/up-password.compiled.html"));
            }
        }
        for (Map.Entry<String, String> entry : data.entrySet()) {
            body = body.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

            // Parseia o HTML
            Document doc = Jsoup.parse(input, "UTF-8");
            title = doc.title();

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
            message.setContent(body, "text/html; charset=UTF-8");

            Transport.send(message); // emvia
            return ResponseEntity.ok("✅ E-mail enviado com sucesso!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
