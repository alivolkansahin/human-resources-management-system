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

    @Value("${spending-service-config.rabbitmq.spending-exchange}")
    private String spendingExchange;

    @Value("${spending-service-config.rabbitmq.get-personnel-id-and-company-id-for-spending-request-queue}")
    private String getPersonnelIdAndCompanyIdForSpendingRequestQueue;

    @Value("${spending-service-config.rabbitmq.get-personnel-id-and-company-id-for-spending-request-binding-key}")
    private String getPersonnelIdAndCompanyIdForSpendingRequestBindingKey;

    @Value("${spending-service-config.rabbitmq.send-spending-status-change-notification-to-personnel-service-queue}")
    private String sendSpendingStatusChangeNotificationToPersonnelServiceQueue;

    @Value("${spending-service-config.rabbitmq.send-spending-status-change-notification-to-personnel-service-binding-key}")
    private String sendSpendingStatusChangeNotificationToPersonnelServiceBindingKey;

    @Value("${spending-service-config.rabbitmq.get-personnel-details-for-spending-request-queue}")
    private String getPersonnelDetailsForSpendingRequestQueue;

    @Value("${spending-service-config.rabbitmq.get-personnel-details-for-spending-request-binding-key}")
    private String getPersonnelDetailsForSpendingRequestBindingKey;

    @Bean
    DirectExchange spendingExchange(){
        return new DirectExchange(spendingExchange);
    }

    @Bean
    Queue getPersonnelIdAndCompanyIdForSpendingRequestQueue(){
        return new Queue(getPersonnelIdAndCompanyIdForSpendingRequestQueue);
    }

    @Bean
    Queue sendSpendingStatusChangeNotificationToPersonnelServiceQueue(){
        return new Queue(sendSpendingStatusChangeNotificationToPersonnelServiceQueue);
    }

    @Bean
    Queue getPersonnelDetailsForSpendingRequestQueue(){
        return new Queue(getPersonnelDetailsForSpendingRequestQueue);
    }

    @Bean
    public Binding bindingGetPersonnelIdAndCompanyIdForSpendingRequestQueue(DirectExchange spendingExchange, Queue getPersonnelIdAndCompanyIdForSpendingRequestQueue){
        return BindingBuilder.bind(getPersonnelIdAndCompanyIdForSpendingRequestQueue).to(spendingExchange).with(getPersonnelIdAndCompanyIdForSpendingRequestBindingKey);
    }

    @Bean
    public Binding bindingSpendingStatusChangeNotificationToPersonnelServiceQueue(DirectExchange spendingExchange, Queue sendSpendingStatusChangeNotificationToPersonnelServiceQueue){
        return BindingBuilder.bind(sendSpendingStatusChangeNotificationToPersonnelServiceQueue).to(spendingExchange).with(sendSpendingStatusChangeNotificationToPersonnelServiceBindingKey);
    }

    @Bean
    public Binding bindingGetPersonnelDetailsForSpendingRequestQueue(DirectExchange spendingExchange, Queue getPersonnelDetailsForSpendingRequestQueue){
        return BindingBuilder.bind(getPersonnelDetailsForSpendingRequestQueue).to(spendingExchange).with(getPersonnelDetailsForSpendingRequestBindingKey);
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
