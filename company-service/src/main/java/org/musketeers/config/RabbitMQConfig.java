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

    /*
    *  1-Produces
    *
    *  2-Consumes
    *
    *  3-Models
    *
    */

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;

    @Value("${rabbitmq.get-company-id-from-supervisor-queue}")
    private String queueName;

    @Value("${rabbitmq.get-company-id-from-supervisor-binding-key}")
    private String bindingKey;

    @Value("${rabbitmq.get-company-supervisors-supervisor-queue}")
    private String getCompanySupervisorsSupervisorQueue;

    @Value("${rabbitmq.get-company-supervisors-supervisor-binding-key}")
    private String getCompanySupervisorsSupervisorBindingKey;


    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }
    @Bean
    Queue registerQueue(){
        return new Queue(queueName);
    }

    @Bean
    Queue getCompanySupervisorsSupervisorQueue(){
        return new Queue(getCompanySupervisorsSupervisorQueue);
    }

    @Bean
    Binding bindingRegisterQueue(DirectExchange exchangeAuth, Queue registerQueue){
        return BindingBuilder.bind(registerQueue).to(exchangeAuth).with(bindingKey);
    }

    @Bean
    public Binding bindingGetCompanySupervisorsSupervisorQueue(DirectExchange exchange, Queue getCompanySupervisorsSupervisorQueue){
        return BindingBuilder.bind(getCompanySupervisorsSupervisorQueue).to(exchange).with(getCompanySupervisorsSupervisorBindingKey);
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
