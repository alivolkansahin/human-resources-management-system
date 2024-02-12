package org.musketeers.config.rabbitmq;

import org.springframework.amqp.core.*;
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

    @Value("${personnel-service-config.rabbitmq.create-personnel-auth-binding-key}")
    private String createPersonnelAuthBindingKey;

    @Value("${personnel-service-config.rabbitmq.create-personnel-company-binding-key}")
    private String createPersonnelCompanyBindingKey;

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
    Binding bindingCreatePersonnelAuthQueue(DirectExchange createPersonnelExchange, Queue createPersonnelAuthQueue) {
        return BindingBuilder.bind(createPersonnelAuthQueue).to(createPersonnelExchange).with(createPersonnelAuthBindingKey);
    }

    @Bean
    Binding bindingCreatePersonnelCompanyQueue(DirectExchange createPersonnelExchange, Queue createPersonnelCompanyQueue) {
        return BindingBuilder.bind(createPersonnelCompanyQueue).to(createPersonnelExchange).with(createPersonnelCompanyBindingKey);
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
