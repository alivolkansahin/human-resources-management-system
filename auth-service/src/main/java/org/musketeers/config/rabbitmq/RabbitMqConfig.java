//package org.musketeers.config.rabbitmq;
//
//
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class RabbitMqConfig {
//
//    @Value("${rabbitmq.auth-exchange}")
//    private String exchange;
//
//    @Value("${rabbitmq.mail-queue-guest}")
//    private String mailQueueName;
//
//    @Value("${rabbitmq.mail-bindingKey}")
//    private String mailBindingKey;
//
//
//    @Bean
//    DirectExchange exchangeAuth(){
//        return new DirectExchange(exchange);
//    }
//
//    @Bean
//    Queue mailQueue(){
//        return new Queue(mailQueueName);
//    }
//    @Bean
//    public Binding bindingMail(Queue mailQueue, DirectExchange exchangeAuth){
//        return BindingBuilder.bind(mailQueue).to(exchangeAuth).with(mailBindingKey);
//    }
//
//
//    /*
//    @Bean
//    MessageConverter messageConverter(){
//        return  new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    RabbitTemplate rabbitTemplate(ConnectionFactory factory){
//        RabbitTemplate template = new RabbitTemplate(factory);
//        template.setMessageConverter(messageConverter());
//        return template;
//    }
//     */
//
//}
