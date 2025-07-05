package com.myproject.sendEmails.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailSender emailSender;

    public EmailController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO request) throws IOException {
        // usa o service para enviar e-mail
        return emailSender.sendTextEmail(
                request.to(),
                request.type(),
                request.date() == null ? new Date() : request.date(),
                request.data()
        );
    }
}
