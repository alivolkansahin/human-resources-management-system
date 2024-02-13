package org.musketeers.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.ActivationGuestModel;
import org.musketeers.rabbitmq.model.CreatePersonnelMailModel;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfiguration; // doc: The main entry point into the FreeMarker API; encapsulates the configuration settings of FreeMarker, also serves as a central template-loading and caching service.

    public void sendMail(ActivationGuestModel model) {
        // Recommended settings for new projects:
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        freemarkerConfiguration.setFallbackOnNullLoopVariable(false);
        freemarkerConfiguration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

        // MimeMessage : Offers support for HTML text content, inline elements such as images, and typical mail attachments.
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Musketeers HR Management System Account Activation");
            mimeMessageHelper.setFrom("avolkan.shn@gmail.com");
            mimeMessageHelper.setTo(model.getEmail());
            mimeMessageHelper.setCc("avolkan.shn@gmail.com");
            mimeMessageHelper.setText(getMailContent(model), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String getMailContent(ActivationGuestModel model) {
        // Create a data-model
        Map<String, String> root = new HashMap<>();
        root.put("id", model.getId());
        root.put("name", model.getName());
        root.put("email", model.getEmail());
        String mailContent;
        try {
            // Get the template (uses cache internally)
            Template temp = freemarkerConfiguration.getTemplate("guest-activation.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(temp, root);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMailForPersonnel(CreatePersonnelMailModel model) {
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        freemarkerConfiguration.setFallbackOnNullLoopVariable(false);
        freemarkerConfiguration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Musketeers HR Management System Account data submission");
            mimeMessageHelper.setFrom("avolkan.shn@gmail.com");
            mimeMessageHelper.setTo(model.getEmail());
            mimeMessageHelper.setCc("avolkan.shn@gmail.com");
            mimeMessageHelper.setText(getMailContentForPersonnel(model), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String getMailContentForPersonnel(CreatePersonnelMailModel model) {
        Map<String, String> root = new HashMap<>();
        root.put("name", model.getName());
        root.put("email", model.getEmail());
        root.put("password", model.getPassword());
        try {
            Template temp = freemarkerConfiguration.getTemplate("personnel-information.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(temp, root);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
