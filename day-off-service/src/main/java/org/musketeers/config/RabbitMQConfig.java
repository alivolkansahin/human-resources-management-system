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

    @Value("${day-off-service-config.rabbitmq.day-off-exchange}")
    private String dayOffExchange;

    @Value("${day-off-service-config.rabbitmq.get-personnel-id-and-company-id-for-day-off-request-queue}")
    private String getPersonnelIdAndCompanyIdForDayOffRequestQueue;

    @Value("${day-off-service-config.rabbitmq.get-personnel-id-and-company-id-for-day-off-request-binding-key}")
    private String getPersonnelIdAndCompanyIdForDayOffRequestBindingKey;

    @Value("${day-off-service-config.rabbitmq.send-day-off-status-change-notification-to-personnel-service-queue}")
    private String sendDayOffStatusChangeNotificationToPersonnelServiceQueue;

    @Value("${day-off-service-config.rabbitmq.send-day-off-status-change-notification-to-personnel-service-binding-key}")
    private String sendDayOffStatusChangeNotificationToPersonnelServiceBindingKey;

    @Value("${day-off-service-config.rabbitmq.get-personnel-details-for-day-off-request-queue}")
    private String getPersonnelDetailsForDayOffRequestQueue;

    @Value("${day-off-service-config.rabbitmq.get-personnel-details-for-day-off-request-binding-key}")
    private String getPersonnelDetailsForDayOffRequestBindingKey;

    @Bean
    DirectExchange dayOffExchange(){
        return new DirectExchange(dayOffExchange);
    }

    @Bean
    Queue getPersonnelIdAndCompanyIdForDayOffRequestQueue(){
        return new Queue(getPersonnelIdAndCompanyIdForDayOffRequestQueue);
    }

    @Bean
    Queue sendDayOffStatusChangeNotificationToPersonnelServiceQueue(){
        return new Queue(sendDayOffStatusChangeNotificationToPersonnelServiceQueue);
    }

    @Bean
    Queue getPersonnelDetailsForDayOffRequestQueue(){
        return new Queue(getPersonnelDetailsForDayOffRequestQueue);
    }

    @Bean
    public Binding bindingGetPersonnelIdAndCompanyIdForDayOffRequestQueue(DirectExchange dayOffExchange, Queue getPersonnelIdAndCompanyIdForDayOffRequestQueue){
        return BindingBuilder.bind(getPersonnelIdAndCompanyIdForDayOffRequestQueue).to(dayOffExchange).with(getPersonnelIdAndCompanyIdForDayOffRequestBindingKey);
    }

    @Bean
    public Binding bindingSendDayOffStatusChangeNotificationToPersonnelServiceQueue(DirectExchange dayOffExchange, Queue sendDayOffStatusChangeNotificationToPersonnelServiceQueue){
        return BindingBuilder.bind(sendDayOffStatusChangeNotificationToPersonnelServiceQueue).to(dayOffExchange).with(sendDayOffStatusChangeNotificationToPersonnelServiceBindingKey);
    }

    @Bean
    public Binding bindingGetPersonnelDetailsForDayOffRequestQueue(DirectExchange dayOffExchange, Queue getPersonnelDetailsForDayOffRequestQueue){
        return BindingBuilder.bind(getPersonnelDetailsForDayOffRequestQueue).to(dayOffExchange).with(getPersonnelDetailsForDayOffRequestBindingKey);
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
