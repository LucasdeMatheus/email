package com.example.sendEmails.Email;

import java.util.Date;
import java.util.List;

public record EmailDTO(
        List<String> to,
        String title,
        String body,
        Date date
) {
}
