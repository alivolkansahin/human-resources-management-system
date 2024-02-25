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

    @Value("${advance-service-config.rabbitmq.advance-exchange}")
    private String advanceExchange;

    @Value("${advance-service-config.rabbitmq.get-personnel-id-and-company-id-for-advance-request-queue}")
    private String getPersonnelIdAndCompanyIdForAdvanceRequestQueue;

    @Value("${advance-service-config.rabbitmq.get-personnel-id-and-company-id-for-advance-request-binding-key}")
    private String getPersonnelIdAndCompanyIdForAdvanceRequestBindingKey;

    @Value("${advance-service-config.rabbitmq.send-advance-status-change-notification-to-personnel-service-queue}")
    private String sendAdvanceStatusChangeNotificationToPersonnelServiceQueue;

    @Value("${advance-service-config.rabbitmq.send-advance-status-change-notification-to-personnel-service-binding-key}")
    private String sendAdvanceStatusChangeNotificationToPersonnelServiceBindingKey;

    @Value("${advance-service-config.rabbitmq.get-personnel-details-for-advance-request-queue}")
    private String getPersonnelDetailsForAdvanceRequestQueue;

    @Value("${advance-service-config.rabbitmq.get-personnel-details-for-advance-request-binding-key}")
    private String getPersonnelDetailsForAdvanceRequestBindingKey;

    @Bean
    DirectExchange advanceExchange(){
        return new DirectExchange(advanceExchange);
    }

    @Bean
    Queue getPersonnelIdAndCompanyIdForAdvanceRequestQueue(){
        return new Queue(getPersonnelIdAndCompanyIdForAdvanceRequestQueue);
    }

    @Bean
    Queue sendAdvanceStatusChangeNotificationToPersonnelServiceQueue(){
        return new Queue(sendAdvanceStatusChangeNotificationToPersonnelServiceQueue);
    }

    @Bean
    Queue getPersonnelDetailsForAdvanceRequestQueue(){
        return new Queue(getPersonnelDetailsForAdvanceRequestQueue);
    }

    @Bean
    public Binding bindingGetPersonnelIdAndCompanyIdForAdvanceRequestQueue(DirectExchange advanceExchange, Queue getPersonnelIdAndCompanyIdForAdvanceRequestQueue){
        return BindingBuilder.bind(getPersonnelIdAndCompanyIdForAdvanceRequestQueue).to(advanceExchange).with(getPersonnelIdAndCompanyIdForAdvanceRequestBindingKey);
    }

    @Bean
    public Binding bindingSendAdvanceStatusChangeNotificationToPersonnelServiceQueue(DirectExchange advanceExchange, Queue sendAdvanceStatusChangeNotificationToPersonnelServiceQueue){
        return BindingBuilder.bind(sendAdvanceStatusChangeNotificationToPersonnelServiceQueue).to(advanceExchange).with(sendAdvanceStatusChangeNotificationToPersonnelServiceBindingKey);
    }

    @Bean
    public Binding bindingGetPersonnelDetailsForAdvanceRequestQueue(DirectExchange advanceExchange, Queue getPersonnelDetailsForAdvanceRequestQueue){
        return BindingBuilder.bind(getPersonnelDetailsForAdvanceRequestQueue).to(advanceExchange).with(getPersonnelDetailsForAdvanceRequestBindingKey);
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
