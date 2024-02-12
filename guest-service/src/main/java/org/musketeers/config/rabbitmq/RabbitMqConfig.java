package org.musketeers.config.rabbitmq;

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
public class RabbitMqConfig {

//    @Value("${guest-service-config.rabbitmq.activation-guest-exchange}")
//    private String exchange;
//
//    @Value("${guest-service-config.rabbitmq.activation-guest-queue}")
//    private String mailQueueName;
//
//    @Value("${guest-service-config.rabbitmq.activation-guest-binding-key}")
//    private String mailBindingKey;
//
//    @Bean
//    DirectExchange exchangeAuth(){
//        return new DirectExchange(exchange);
//    }
//    @Bean
//    Queue mailQueue(){
//        return new Queue(mailQueueName);
//    }
//    @Bean
//    public Binding bindingMail(DirectExchange exchangeAuth, Queue mailQueue){
//        return BindingBuilder.bind(mailQueue).to(exchangeAuth).with(mailBindingKey);
//    }
//
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
