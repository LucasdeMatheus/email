package com.example.sendEmails.Email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailSender emailSender;

    public EmailController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO request) {
        // usa o service para enviar e-mail
        return emailSender.sendTextEmail(
                request.to(),
                request.title(),
                request.body(),
                request.date() == null ? new Date() : request.date()
        );
    }
}
