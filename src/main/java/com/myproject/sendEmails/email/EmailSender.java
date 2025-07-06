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
        System.out.println("Iniciando envio de email para: " + to);
        System.out.println("Tipo de email: " + type);

        // define as configurações
        Session session = Session.getInstance(config.toProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("Autenticando email: " + config.getUsername());
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });

        String title = "";
        String body = "";
        File input = null;
        String fileUrl = "C:/Users/ffgus/desafio-auth-java/user-api/libs/email-library/src/main/resources/emails/";
        try {
            switch (type){
                case WELLCOME ->{
                    input = new File(fileUrl + "confirm-email.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "welcome.compiled.html"));
                }
                case VALIDEMAIL -> {
                    input = new File(fileUrl + "confirm-email.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "confirm-email.compiled.html"));
                }
                case UPPASSWORD -> {
                    input = new File(fileUrl + "up-password.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "up-password.compiled.html"));
                }
                case SUCESSCHANGEPASSWORD -> {
                    input = new File(fileUrl + "sucesschange-password.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "sucesschange-password.compiled.html"));
                }
                case UPEMAIL -> {
                    input = new File(fileUrl + "up-email.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "up-email.compiled.html"));
                }
                case SUCESSCHANGEEMAIL -> {
                    input = new File(fileUrl + "sucesschange-email.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "sucesschange-email.compiled.html"));
                }
                case DELETEUSER -> {
                    input = new File(fileUrl + "delete-user.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "delete-user.compiled.html"));
                }
                case DELETESUCESSUSER -> {
                    input = new File(fileUrl + "deletesucess-user.compiled.html");
                    body = Files.readString(Paths.get(fileUrl + "deletesucess-user.compiled.html"));
                }
            }

            System.out.println("Arquivo de template carregado: " + input.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo de template: " + e.getMessage());
            throw e;
        }

        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println("Substituindo placeholder: {{" + entry.getKey() + "}} por " + entry.getValue());
            body = body.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        // Parseia o HTML
        Document doc = Jsoup.parse(input, "UTF-8");
        title = doc.title();
        System.out.println("Título do email: " + title);

        session.setDebug(config.isDebug());

        try {
            // criação da mensagem
            MimeMessage message = new MimeMessage(session);
            System.out.println("From email: " + config.getUsername());
            System.out.println("To email: " + to);

            message.setFrom(new InternetAddress(config.getUsername())); // email do remetente
            System.out.println("Remetente setado: " + config.getUsername());

            // adicionando destinatários
            InternetAddress[] recipients = to.stream()
                    .map(email -> {
                        try {
                            InternetAddress address = new InternetAddress(email.trim());
                            address.validate();  // Valida o formato do email
                            System.out.println("Destinatário válido: " + email);
                            return address;
                        } catch (Exception e) {
                            System.out.println("Email inválido: " + email);
                            throw new RuntimeException("Email inválido: " + email, e);
                        }
                    })
                    .toArray(InternetAddress[]::new);

            message.setRecipients(Message.RecipientType.TO, recipients); // "para: destinatarios..."
            message.setSubject(title, "UTF-8"); // título
            message.setText(body, "UTF-8"); // mensagem em texto (mas depois substituído pelo html)
            message.setSentDate(date); // data de envio
            message.setContent(body, "text/html; charset=UTF-8");

            System.out.println("Mensagem pronta para envio, enviando...");
            Transport.send(message); // envia
            System.out.println("Email enviado com sucesso!");
            return ResponseEntity.ok("✅ E-mail enviado com sucesso!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar e-mail: " + e.getMessage());
            return ResponseEntity.status(500).body("❌ Erro ao enviar e-mail: " + e.getMessage());
        }
    }

}
