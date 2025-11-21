package com.eprogs.raiseme.service.emailServices;

import com.eprogs.raiseme.constant.EmailTemplateConstant;
import com.eprogs.raiseme.dto.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final TemplateService templateService;

    private final JavaMailSender mailSender;

    @Async
    public void sendHTMLMailWithClientSMTPCred(EmailDetails details, EmailTemplateConstant template) {
        String subject = null;
        String htmlContent = null;

        try {
            htmlContent = templateService.processTemplate(template, details.getVariables());
            subject = template.getSubject();


            sendHtmlSMTPMail(details.getRecipient(), subject, htmlContent);

        } catch (Exception e) {
            log.error("Template processing failed for: {}", details.getRecipient(), e);
            try {
                sendHtmlSMTPMail(details.getRecipient(), subject, htmlContent);
            } catch (Exception ex) {
                log.error("mail failed for: {}", details.getRecipient(), e);

            }

        }
    }

    private void sendHtmlSMTPMail(
            String recipient,
            String subject,
            String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setFrom("your-email@gmail.com");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

}
