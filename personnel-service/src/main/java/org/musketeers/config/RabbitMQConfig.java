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

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String createPersonnelExchange;

    @Value("${personnel-service-config.rabbitmq.create-personnel-auth-queue}")
    private String createPersonnelAuthQueue;

    @Value("${personnel-service-config.rabbitmq.create-personnel-company-queue}")
    private String createPersonnelCompanyQueue;

    @Value("${personnel-service-config.rabbitmq.get-company-id-supervisor-queue}")
    private String getCompanyIdSupervisorQueue;

    @Value("${personnel-service-config.rabbitmq.get-company-details-by-personnel-queue}")
    private String getCompanyDetailsByPersonnelQueue;

    @Value("${personnel-service-config.rabbitmq.get-company-details-by-personnel-binding-key}")
    private String getCompanyDetailsByPersonnelBindingKey;

    @Value("${personnel-service-config.rabbitmq.create-personnel-auth-binding-key}")
    private String createPersonnelAuthBindingKey;

    @Value("${personnel-service-config.rabbitmq.create-personnel-company-binding-key}")
    private String createPersonnelCompanyBindingKey;

    @Value("${personnel-service-config.rabbitmq.get-company-id-supervisor-binding-key}")
    private String getCompanyIdSupervisorBindingKey;

    @Value("${personnel-service-config.rabbitmq.update-personnel-by-id-auth-queue}")
    private String updatePersonnelByIdAuthQueue;

    @Value("${personnel-service-config.rabbitmq.update-personnel-by-id-auth-binding-key}")
    private String updatePersonnelByIdAuthBindingKey;

    @Value("${personnel-service-config.rabbitmq.update-personnel-by-id-supervisor-queue}")
    private String updatePersonnelByIdSupervisorQueue;

    @Value("${personnel-service-config.rabbitmq.update-personnel-by-id-supervisor-binding-key}")
    private String updatePersonnelByIdSupervisorBindingKey;

    @Value("${personnel-service-config.rabbitmq.send-day-off-status-change-notification-to-mail-service-queue}")
    private String sendDayOffStatusChangeNotificationToMailServiceQueue;

    @Value("${personnel-service-config.rabbitmq.send-day-off-status-change-notification-to-mail-service-binding-key}")
    private String sendDayOffStatusChangeNotificationToMailServiceBindingKey;

    @Bean
    DirectExchange createPersonnelExchange() {
        return new DirectExchange(createPersonnelExchange);
    }

    @Bean
    Queue createPersonnelAuthQueue(){
        return new Queue(createPersonnelAuthQueue);
    }

    @Bean
    Queue createPersonnelCompanyQueue(){
        return new Queue(createPersonnelCompanyQueue);
    }

    @Bean
    Queue getCompanyIdSupervisorQueue(){
        return new Queue(getCompanyIdSupervisorQueue);
    }
    @Bean
    Queue getCompanyDetailsByPersonnelQueue(){
        return new Queue(getCompanyDetailsByPersonnelQueue);
    }

    @Bean
    Queue updatePersonnelByIdAuthQueue(){
        return new Queue(updatePersonnelByIdAuthQueue);
    }

    @Bean
    Queue updatePersonnelByIdSupervisorQueue(){
        return new Queue(updatePersonnelByIdSupervisorQueue);
    }

    @Bean
    Queue sendDayOffStatusChangeNotificationToMailServiceQueue(){
        return new Queue(sendDayOffStatusChangeNotificationToMailServiceQueue);
    }

    @Bean
    Binding bindingCreatePersonnelAuthQueue(DirectExchange createPersonnelExchange, Queue createPersonnelAuthQueue) {
        return BindingBuilder.bind(createPersonnelAuthQueue).to(createPersonnelExchange).with(createPersonnelAuthBindingKey);
    }

    @Bean
    Binding bindingCreatePersonnelCompanyQueue(DirectExchange createPersonnelExchange, Queue createPersonnelCompanyQueue) {
        return BindingBuilder.bind(createPersonnelCompanyQueue).to(createPersonnelExchange).with(createPersonnelCompanyBindingKey);
    }

    @Bean
    Binding bindingGetCompanyIdSupervisorQueue(DirectExchange createPersonnelExchange, Queue getCompanyIdSupervisorQueue) {
        return BindingBuilder.bind(getCompanyIdSupervisorQueue).to(createPersonnelExchange).with(getCompanyIdSupervisorBindingKey);
    }
    @Bean
    Binding bindingGetCompanyDetailsByPersonnelQueue(DirectExchange createPersonnelExchange, Queue getCompanyDetailsByPersonnelQueue) {
        return BindingBuilder.bind(getCompanyDetailsByPersonnelQueue).to(createPersonnelExchange).with(getCompanyDetailsByPersonnelBindingKey);
    }

    @Bean
    public Binding bindingUpdatePersonnelByIdAuthQueue(DirectExchange createPersonnelExchange, Queue updatePersonnelByIdAuthQueue){
        return BindingBuilder.bind(updatePersonnelByIdAuthQueue).to(createPersonnelExchange).with(updatePersonnelByIdAuthBindingKey);
    }

    @Bean
    public Binding bindingUpdatePersonnelByIdSupervisorQueue(DirectExchange createPersonnelExchange, Queue updatePersonnelByIdSupervisorQueue){
        return BindingBuilder.bind(updatePersonnelByIdSupervisorQueue).to(createPersonnelExchange).with(updatePersonnelByIdSupervisorBindingKey);
    }

    @Bean
    public Binding bindingSendDayOffStatusChangeNotificationToMailServiceQueue(DirectExchange createPersonnelExchange, Queue sendDayOffStatusChangeNotificationToMailServiceQueue){
        return BindingBuilder.bind(sendDayOffStatusChangeNotificationToMailServiceQueue).to(createPersonnelExchange).with(sendDayOffStatusChangeNotificationToMailServiceBindingKey);
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
