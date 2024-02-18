package org.musketeers.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /*
    *  1-Produces
    *
    *  2-Consumes
    *
    *  3-Models
    *
    */

    @Value("${admin-service-config.rabbitmq.admin-exchange}")
    String exchangeName;

    @Value("${admin-service-config.rabbitmq.get-supervisor-queue}")
    String queueName;

    @Value("${admin-service-config.rabbitmq.get-supervisor-binding-key}")
    String bindingKey;

    @Value("${admin-service-config.rabbitmq.admin-supervisor-registration-decision-exchange}")
    private String supervisorDecisionExchange;

    @Value("${admin-service-config.rabbitmq.registration-decision-supervisor-queue}")
    private String supervisorDecisionSupervisorQueue;

    @Value("${admin-service-config.rabbitmq.registration-decision-auth-queue}")
    private String supervisorDecisionAuthQueue;

    @Value("${admin-service-config.rabbitmq.admin-register-auth-queue}")
    private String adminRegisterAuthQueue;

    @Value("${admin-service-config.rabbitmq.admin-register-auth-binding-key}")
    private String adminRegisterAuthBindingKey;

    @Value("${admin-service-config.rabbitmq.admin-get-all-pending-comments-comment-queue}")
    private String adminGetAllPendingCommentsCommentQueue;

    @Value("${admin-service-config.rabbitmq.admin-get-all-pending-comments-comment-binding-key}")
    private String adminGetAllPendingCommentsCommentBindingKey;

    @Value("${admin-service-config.rabbitmq.admin-handle-comment-decision-queue}")
    private String adminHandleCommentDecisionQueue;

    @Value("${admin-service-config.rabbitmq.admin-handle-comment-decision-binding-key}")
    private String adminHandleCommentDecisionBindingKey;

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    FanoutExchange supervisorDecisionExchange() {
        return new FanoutExchange(supervisorDecisionExchange);
    }

    @Bean
    Queue registerQueue(){
        return new Queue(queueName);
    }

    @Bean
    Queue supervisorDecisionSupervisorQueue(){
        return new Queue(supervisorDecisionSupervisorQueue);
    }

    @Bean
    Queue supervisorDecisionAuthQueue(){
        return new Queue(supervisorDecisionAuthQueue);
    }

    @Bean
    Queue adminRegisterAuthQueue(){
        return new Queue(adminRegisterAuthQueue);
    }

    @Bean
    Queue adminGetAllPendingCommentsCommentQueue(){
        return new Queue(adminGetAllPendingCommentsCommentQueue);
    }
    @Bean
    Queue adminHandleCommentDecisionQueue(){
        return new Queue(adminHandleCommentDecisionQueue);
    }

    @Bean
    Binding bindingRegisterQueue(DirectExchange exchangeAdmin, Queue registerQueue){
        return BindingBuilder.bind(registerQueue).to(exchangeAdmin).with(bindingKey);
    }

    @Bean
    Binding bindingAdminRegisterAuthQueue(DirectExchange exchangeAdmin, Queue adminRegisterAuthQueue){
        return BindingBuilder.bind(adminRegisterAuthQueue).to(exchangeAdmin).with(adminRegisterAuthBindingKey);
    }

    @Bean
    Binding bindingSuperVisorDecisionSupervisorQueue(FanoutExchange supervisorDecisionExchange, Queue supervisorDecisionSupervisorQueue) {
        return BindingBuilder.bind(supervisorDecisionSupervisorQueue).to(supervisorDecisionExchange);
    }

    @Bean
    Binding bindingSuperVisorDecisionAuthQueue(FanoutExchange supervisorDecisionExchange, Queue supervisorDecisionAuthQueue) {
        return BindingBuilder.bind(supervisorDecisionAuthQueue).to(supervisorDecisionExchange);
    }

    @Bean
    Binding bindingAdminGetAllPendingCommentsCommentQueue(DirectExchange exchangeAdmin, Queue adminGetAllPendingCommentsCommentQueue) {
        return BindingBuilder.bind(adminGetAllPendingCommentsCommentQueue).to(exchangeAdmin).with(adminGetAllPendingCommentsCommentBindingKey);
    }

    @Bean
    Binding bindingAdminHandleCommentDecisionQueue(DirectExchange exchangeAdmin, Queue adminHandleCommentDecisionQueue) {
        return BindingBuilder.bind(adminHandleCommentDecisionQueue).to(exchangeAdmin).with(adminHandleCommentDecisionBindingKey);
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
