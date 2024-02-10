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

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-exchange}")
    private String supervisorActivationExchange;

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-company-queue}")
    private String supervisorActivationCompanyQueue;

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-company-binding-key}")
    private String supervisorActivationCompanyBindingKey;

    @Bean
    DirectExchange supervisorActivationExchange(){
        return new DirectExchange(supervisorActivationExchange);
    }

    @Bean
    Queue supervisorActivationCompanyQueue(){
        return new Queue(supervisorActivationCompanyQueue);
    }

    @Bean
    public Binding bindingSupervisorActivationCompanyQueue(DirectExchange supervisorActivationExchange, Queue supervisorActivationCompanyQueue){
        return BindingBuilder.bind(supervisorActivationCompanyQueue).to(supervisorActivationExchange).with(supervisorActivationCompanyBindingKey);
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
