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

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }
    @Bean
    Queue registerQueue(){
        return new Queue(queueName);
    }
    @Bean
    Binding bindingRegisterQueue(DirectExchange exchangeAdmin, Queue getSupervisorQueue){
        return BindingBuilder.bind(getSupervisorQueue).to(exchangeAdmin).with(bindingKey);
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
