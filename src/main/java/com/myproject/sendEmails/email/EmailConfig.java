package com.myproject.sendEmails.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfig {

    private String username;
    private String password;
    private String host = "smtp.gmail.com";
    private int port = 587;
    private boolean auth = true;
    private boolean starttls = true;
    private boolean debug = false;

    public EmailConfig() {
    }

    // GETTERS
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public boolean isAuth() { return auth; }
    public boolean isStarttls() { return starttls; }
    public boolean isDebug() { return debug; }

    // SETTERS (importante para o bind funcionar)
    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }
    public void setHost(String host) { this.host = host; }
    public void setPort(int port) { this.port = port; }
    public void setAuth(boolean auth) { this.auth = auth; }
    public void setStarttls(boolean starttls) { this.starttls = starttls; }
    public void setDebug(boolean debug) { this.debug = debug; }

    public Properties toProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", String.valueOf(auth));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        return props;
    }
}
