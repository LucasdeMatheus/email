package com.myproject.sendEmails.email;

import java.util.Date;
import java.util.List;
import java.util.Map;

public record EmailDTO(
        List<String> to,
        Map<String, String> data,
        Type type,
        Date date
) {
}
