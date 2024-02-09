//package org.musketeers.config.rabbitmq;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//    // Autha yönlendirilecek.
//    @Value("${guest-service-config.rabbitmq.auth-exchange}")
//    private String exchange;
//
//    @Value("${guest-service-config.rabbitmq.register-guest-queue}")
//    private String registerGuestQueueName;
//
//    @Value("${guest-service-config.rabbitmq.register-guest-bindingKey}")
//    private String registerGuestBindingKey;
//
//    // activation olmazsa buna gerek yok.
//    @Value("${guest-service-config.rabbitmq.activation-guest-queue}")
//    private String activationGuestQueueName;
//
//    @Value("${guest-service-config.rabbitmq.activation-guest-bindingKey}")
//    private String activationGuestBindingKey;
//
//    @Bean
//    DirectExchange exchangeAuth(){
//        return new DirectExchange(exchange);
//    }
//    @Bean
//    Queue registerGuestQueue(){
//        return new Queue(registerGuestQueueName);
//    }
//    @Bean
//    Queue activationGuestQueue(){
//        return new Queue(activationGuestQueueName);
//    }
//
//    @Bean
//    public Binding bindingRegisterGuest(Queue registerGuestQueue, DirectExchange exchangeAuth){
//        return BindingBuilder.bind(registerGuestQueue).to(exchangeAuth).with(registerGuestBindingKey);
//    }
//
//    @Bean
//    public Binding bindingActivationGuest(Queue activationGuestQueue, DirectExchange exchangeAuth){
//        return BindingBuilder.bind(activationGuestQueue).to(exchangeAuth).with(activationGuestBindingKey);
//    }
//
//    @Bean
//    MessageConverter messageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    RabbitTemplate rabbitTemplate(ConnectionFactory factory){
//        RabbitTemplate template = new RabbitTemplate(factory);
//        template.setMessageConverter(messageConverter());
//        return template;
//    }
//
//}
