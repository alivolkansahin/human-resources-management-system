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

    @Value("adminExchange")
    String exchangeName;

    @Value("getSupervisorQueue")
    String queueName;

    @Value("getSupervisorBindingKey")
    String bindingKey;

    @Value("${admin-service-config.rabbitmq.admin-supervisor-registration-decision-exchange}")
    String supervisorDecisionExchange;

    @Value("${admin-service-config.rabbitmq.registration-decision-supervisor-queue}")
    String supervisorDecisionSupervisorQueue;

    @Value("${admin-service-config.rabbitmq.registration-decision-auth-queue}")
    String supervisorDecisionAuthQueue;

//    @Value("${admin-service-config.rabbitmq.registration-decision-supervisor-bindingKey}")
//    String supervisorDecisionSupervisorBindingKey;
//
//    @Value("${admin-service-config.rabbitmq.registration-decision-auth-bindingKey}")
//    String supervisorDecisionAuthBindingKey;

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
    Binding bindingRegisterQueue(DirectExchange exchangeAdmin, Queue registerQueue){
        return BindingBuilder.bind(registerQueue).to(exchangeAdmin).with(bindingKey);
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
