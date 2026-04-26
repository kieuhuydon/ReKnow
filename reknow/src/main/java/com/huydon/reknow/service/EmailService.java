package com.huydon.reknow.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendReminderEmail(String to, String userName,
                                  String bookTitle, String noteContent){
        //email service gọi từ scheduler, hoạt động ngầm global exception không bắt được
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("bookTitle", bookTitle);
            context.setVariable("noteContent", noteContent);

            // render temple thanh HTML string
            String htmlContent = templateEngine.process("reminder-email", context);

            // tạo email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Reknow - Ghi chú hôm nay của bạn");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        }catch(Exception e){
            log.error("Failed to send reminder email to {}. Error: {}", to, e.getMessage());

        }
    }



}
