package org.musketeers.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${comment-service-config.rabbitmq.comment-exchange}")
    private String commentExchange;

    @Value("${comment-service-config.rabbitmq.add-comment-personnel-queue}")
    private String addCommentPersonnelQueue;

    @Value("${comment-service-config.rabbitmq.add-comment-personnel-binding-key}")
    private String addCommentPersonnelBindingKey;

    @Value("${comment-service-config.rabbitmq.get-personnel-details-by-comment-personnel-queue}")
    private String getPersonnelDetailsByCommentPersonnelQueue;

    @Value("${comment-service-config.rabbitmq.get-personnel-details-by-comment-personnel-binding-key}")
    private String getPersonnelDetailsByCommentPersonnelBindingKey;

    @Value("${comment-service-config.rabbitmq.get-company-details-by-comment-company-queue}")
    private String getCompanyDetailsByCommentCompanyQueue;

    @Value("${comment-service-config.rabbitmq.get-company-details-by-comment-company-binding-key}")
    private String getCompanyDetailsByCommentCompanyBindingKey;

    @Bean
    DirectExchange commentExchange() {
        return new DirectExchange(commentExchange);
    }

    @Bean
    Queue addCommentPersonnelQueue(){
        return new Queue(addCommentPersonnelQueue);
    }

    @Bean
    Queue getPersonnelDetailsByCommentPersonnelQueue(){
        return new Queue(getPersonnelDetailsByCommentPersonnelQueue);
    }

    @Bean
    Queue getCompanyDetailsByCommentCompanyQueue(){
        return new Queue(getCompanyDetailsByCommentCompanyQueue);
    }

    @Bean
    Binding bindingAddCommentPersonnelQueue(DirectExchange commentExchange, Queue addCommentPersonnelQueue) {
        return BindingBuilder.bind(addCommentPersonnelQueue).to(commentExchange).with(addCommentPersonnelBindingKey);
    }

    @Bean
    Binding bindingGetPersonnelDetailsByCommentPersonnelQueue(DirectExchange commentExchange, Queue getPersonnelDetailsByCommentPersonnelQueue) {
        return BindingBuilder.bind(getPersonnelDetailsByCommentPersonnelQueue).to(commentExchange).with(getPersonnelDetailsByCommentPersonnelBindingKey);
    }

    @Bean
    Binding bindingGetCompanyDetailsByCommentCompanyQueue(DirectExchange commentExchange, Queue getCompanyDetailsByCommentCompanyQueue) {
        return BindingBuilder.bind(getCompanyDetailsByCommentCompanyQueue).to(commentExchange).with(getCompanyDetailsByCommentCompanyBindingKey);
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
