package com.myproject.sendEmails.email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        System.out.println("Iniciando envio de email para: " + to);
        System.out.println("Tipo de email: " + type);

        // Mapeia o nome do arquivo com base no Enum
        String filename = switch (type) {
            case WELLCOME -> "welcome.compiled.html";
            case VALIDEMAIL -> "confirm-email.compiled.html";
            case UPPASSWORD -> "up-password.compiled.html";
            case SUCESSCHANGEPASSWORD -> "sucesschange-password.compiled.html";
            case UPEMAIL -> "up-email.compiled.html";
            case SUCESSCHANGEEMAIL -> "sucesschange-email.compiled.html";
            case DELETEUSER -> "delete-user.compiled.html";
            case DELETESUCESSUSER -> "deletesucess-user.compiled.html";
        };

        String body = "";
        try {
            // Carrega o arquivo dinamicamente a partir do Classpath (src/main/resources/emails/)
            ClassPathResource resource = new ClassPathResource("emails/" + filename);
            body = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Template " + filename + " carregado com sucesso do classpath.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo de template no classpath: " + e.getMessage());
            throw e;
        }

        // Substitui as variáveis (placeholders) no HTML
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                System.out.println("Substituindo placeholder: {{" + entry.getKey() + "}} por " + entry.getValue());
                body = body.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        // Extrai o título diretamente da string do HTML com JSoup
        Document doc = Jsoup.parse(body);
        String title = doc.title();
        System.out.println("Título do email: " + title);

        // Configuração e envio da sessão Jakarta Mail
        Session session = Session.getInstance(config.toProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
        session.setDebug(config.isDebug());

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getUsername()));

            InternetAddress[] recipients = to.stream()
                    .map(email -> {
                        try {
                            InternetAddress address = new InternetAddress(email.trim());
                            address.validate();
                            return address;
                        } catch (Exception e) {
                            throw new RuntimeException("Email inválido: " + email, e);
                        }
                    })
                    .toArray(InternetAddress[]::new);

            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setSubject(title, "UTF-8");
            message.setSentDate(date != null ? date : new Date());
            message.setContent(body, "text/html; charset=UTF-8");

            System.out.println("Enviando e-mail...");
            Transport.send(message);
            System.out.println("Email enviado com sucesso!");

            return ResponseEntity.ok("✅ E-mail enviado com sucesso!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
