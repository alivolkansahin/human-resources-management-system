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

    @Value("${supervisor-service-config.rabbitmq.create-personnel-from-supervisor-queue}")
    private String createPersonnelFromSupervisorQueue;

    @Value("${supervisor-service-config.rabbitmq.create-personnel-from-supervisor-binding-key}")
    private String createPersonnelFromSupervisorBindingKey;

    @Bean
    DirectExchange supervisorActivationExchange(){
        return new DirectExchange(supervisorActivationExchange);
    }

    @Bean
    Queue supervisorActivationCompanyQueue(){
        return new Queue(supervisorActivationCompanyQueue);
    }

    @Bean
    Queue createPersonnelFromSupervisorQueue(){
        return new Queue(createPersonnelFromSupervisorQueue);
    }

    @Bean
    public Binding bindingSupervisorActivationCompanyQueue(DirectExchange supervisorActivationExchange, Queue supervisorActivationCompanyQueue){
        return BindingBuilder.bind(supervisorActivationCompanyQueue).to(supervisorActivationExchange).with(supervisorActivationCompanyBindingKey);
    }

    @Bean
    public Binding bindingCreatePersonnelFromSupervisorQueue(DirectExchange supervisorActivationExchange, Queue createPersonnelFromSupervisorQueue){
        return BindingBuilder.bind(createPersonnelFromSupervisorQueue).to(supervisorActivationExchange).with(createPersonnelFromSupervisorBindingKey);
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
