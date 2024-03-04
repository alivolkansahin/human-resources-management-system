package org.musketeers.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    // doc: The main entry point into the FreeMarker API; encapsulates the configuration settings of FreeMarker, also serves as a central template-loading and caching service.
    private final Configuration freemarkerConfiguration;

    @Value("${mail-service-config.mail.gateway-url}")
    private String gatewayUrl;

    private void configureFreemarker() {
        // Recommended settings for new projects:
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        freemarkerConfiguration.setFallbackOnNullLoopVariable(false);
        freemarkerConfiguration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
    }

    public void sendActivationMailToGuest(ActivationGuestModel model) {
        configureFreemarker();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Musketeers HR Management System Account Activation");
            mimeMessageHelper.setFrom("infomusketeershmrs@gmail.com");
            mimeMessageHelper.setTo(model.getEmail());
            mimeMessageHelper.setText(getActivationMailContent(model), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String getActivationMailContent(ActivationGuestModel model) {
        Map<String, String> root = new HashMap<>();
        root.put("id", model.getId());
        root.put("name", model.getName());
        root.put("email", model.getEmail());
        root.put("gatewayUrl", gatewayUrl);
        try {
            Template temp = freemarkerConfiguration.getTemplate("guest-activation.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(temp, root);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAccountCreatedInformationToPersonnel(CreatePersonnelMailModel model) {
        configureFreemarker();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Musketeers HR Management System Account Created");
            mimeMessageHelper.setFrom("musketeershmrs@gmail.com");
            mimeMessageHelper.setTo(model.getEmail());
            mimeMessageHelper.setText(getAccountCreatedInformationMailContent(model), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String getAccountCreatedInformationMailContent(CreatePersonnelMailModel model) {
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

    public void sendDayOffRequestStatusChangeMailToPersonnel(SendDayOffStatusChangeMailModel model) {
        configureFreemarker();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Regarding Your Day Off Request Dated " + Instant.ofEpochMilli(model.getRequestCreatedAt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .toString());
            mimeMessageHelper.setFrom("musketeershmrs@gmail.com");
            mimeMessageHelper.setTo(model.getEmail());
            mimeMessageHelper.setText(getDayOffRequestStatusChangeMailContent(model), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String getDayOffRequestStatusChangeMailContent(SendDayOffStatusChangeMailModel model) {
        Map<String, String> root = new HashMap<>();
        root.put("name", model.getName());
        root.put("lastName", model.getLastName());
        root.put("requestCreatedAt", Instant.ofEpochMilli(model.getRequestCreatedAt())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString());
        root.put("requestReason", model.getRequestReason());
        root.put("requestDescription", model.getRequestDescription());
        root.put("requestStartDate", model.getRequestStartDate().toString());
        root.put("requestEndDate", model.getRequestEndDate().toString());
        root.put("requestStatus", model.getUpdatedStatus());
        root.put("requestUpdatedAt", Instant.ofEpochMilli(model.getRequestUpdatedAt())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString());
        try {
            Template temp = freemarkerConfiguration.getTemplate("day-off-request-update.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(temp, root);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAdvanceRequestStatusChangeMailToPersonnel(SendAdvanceStatusChangeMailModel model) {
        configureFreemarker();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Regarding Your Advance Request Dated " + Instant.ofEpochMilli(model.getRequestCreatedAt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .toString());
            mimeMessageHelper.setFrom("musketeershmrs@gmail.com");
            mimeMessageHelper.setTo(model.getEmail());
            mimeMessageHelper.setText(getAdvanceRequestStatusChangeMailContent(model), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String getAdvanceRequestStatusChangeMailContent(SendAdvanceStatusChangeMailModel model) {
        Map<String, String> root = new HashMap<>();
        root.put("name", model.getName());
        root.put("lastName", model.getLastName());
        root.put("requestCreatedAt", Instant.ofEpochMilli(model.getRequestCreatedAt())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString());
        root.put("requestDescription", model.getRequestDescription());
        root.put("requestAmount", model.getRequestAmount().toString());
        root.put("requestStatus", model.getUpdatedStatus());
        root.put("requestUpdatedAt", Instant.ofEpochMilli(model.getRequestUpdatedAt())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString());
        try {
            Template temp = freemarkerConfiguration.getTemplate("advance-request-update.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(temp, root);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSpendingRequestStatusChangeMailToPersonnel(SendSpendingStatusChangeMailModel model) {
        configureFreemarker();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject("Regarding Your Spending Request Dated " + Instant.ofEpochMilli(model.getRequestCreatedAt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .toString());
            mimeMessageHelper.setFrom("musketeershmrs@gmail.com");
            mimeMessageHelper.setTo(model.getEmail());

            for (String attachmentUrl : model.getRequestAttachments()) {
                String fileName = attachmentUrl.substring(attachmentUrl.lastIndexOf("/") + 1);
                mimeMessageHelper.addAttachment(fileName, new UrlResource(attachmentUrl));
            }

            mimeMessageHelper.setText(getSpendingRequestStatusChangeMailContent(model), true);
        } catch (MessagingException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String getSpendingRequestStatusChangeMailContent(SendSpendingStatusChangeMailModel model) {
        Map<String, String> root = new HashMap<>();
        root.put("name", model.getName());
        root.put("lastName", model.getLastName());
        root.put("requestCreatedAt", Instant.ofEpochMilli(model.getRequestCreatedAt())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString());
        root.put("requestReason", model.getRequestReason());
        root.put("requestDescription", model.getRequestDescription());
        root.put("requestAmount", model.getRequestAmount().toString());
        root.put("requestCurrency", model.getRequestCurrency());
        root.put("requestSpendingDate", model.getRequestSpendingDate().toString());
        root.put("requestStatus", model.getUpdatedStatus());
        root.put("requestUpdatedAt", Instant.ofEpochMilli(model.getRequestUpdatedAt())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString());
        try {
            Template temp = freemarkerConfiguration.getTemplate("spending-request-update.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(temp, root);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

}
