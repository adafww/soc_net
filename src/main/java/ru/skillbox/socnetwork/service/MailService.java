package ru.skillbox.socnetwork.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import ru.skillbox.socnetwork.logging.DebugLogs;

import java.util.Properties;

@Service
@RequiredArgsConstructor

public class MailService {

  @Getter
  @Value("${skillbox.app.mail.user}")
  private String username;

  @Getter
  @Value("${skillbox.app.mail.password}")
  private String password;

  private JavaMailSender getMailSender(){
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.yandex.ru");
    mailSender.setPort(465);
    mailSender.setUsername(getUsername());
    mailSender.setPassword(getPassword());

    Properties properties = mailSender.getJavaMailProperties();
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.ssl.enable", "true");

    return mailSender;
  }


  public void send(String email, String subject, String text) {
    JavaMailSender mailSender = getMailSender();
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(getUsername().concat("@yandex.com"));
    message.setTo(email);
    message.setSubject(subject);
    message.setText(text);

//    mailSender.send(message); //TODO: временно отключено до преодоления спам-фильтра
  }
}
