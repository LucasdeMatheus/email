package com.example.sendEmails.Email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@ConfigurationProperties(prefix = "email")
public class EmailConfig {

    private String username;
    private String password;
    private String host = "smtp.gmail.com";
    private int port = 587;
    private boolean auth = true;
    private boolean starttls = true;
    private boolean debug = false;

    public EmailConfig(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Properties toProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", String.valueOf(auth));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        return props;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isDebug() { return debug; }

}
