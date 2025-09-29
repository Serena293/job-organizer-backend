package com.project.job_organizer.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${EMAIL_USER}")
    private String emailFrom;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
 try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom(emailFrom);

        mailSender.send(message);
        System.out.println("Email sent to " + to);
    } catch (MessagingException e) {
        e.printStackTrace();
        System.out.println("Email not sent");
    }


}}
