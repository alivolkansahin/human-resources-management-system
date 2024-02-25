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

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.mail-queue-guest}")
    private String mailQueueName;

    @Value("${rabbitmq.mail-bindingKey}")
    private String mailBindingKey;

    @Value("${rabbitmq.register-guest-queue}")
    private String registerGuestQueueName;
    @Value("${rabbitmq.register-guest-bindingKey}")
    private String registerGuestBindingKey;

    @Value("${rabbitmq.register-supervisor-queue}")
    private String registerSupervisorQueueName;
    @Value("${rabbitmq.register-supervisor-bindingKey}")
    private String registerSupervisorBindingKey;

    @Value("${rabbitmq.register-guest-activation-queue}")
    private String registerGuestActivationQueueName;

    @Value("${rabbitmq.register-guest-activation-bindingKey}")
    private String registerGuestActivationBindingKey;

    @Value("${rabbitmq.mail-queue-personnel}")
    private String mailPersonnelQueueName;

    @Value("${rabbitmq.mail-bindingKey-personnel}")
    private String mailPersonnelBindingKey;

    @Value("${rabbitmq.company-status-check-queue}")
    private String companyStatusCheckQueue;

    @Value("${rabbitmq.company-status-check-bindingKey}")
    private String companyStatusCheckBindingKey;

    @Value("${rabbitmq.get-company-id-for-user-login-queue}")
    private String getCompanyIdForUserLoginQueue;

    @Value("${rabbitmq.get-company-id-for-user-login-bindingKey}")
    private String getCompanyIdForUserLoginBindingKey;

    @Value("${rabbitmq.search-for-company-name-queue}")
    private String searchForCompanyNameQueue;

    @Value("${rabbitmq.search-for-company-name-bindingKey}")
    private String searchForCompanyNameBindingKey;


    @Bean
    DirectExchange exchangeAuth(){
        return new DirectExchange(exchange);
    }




    @Bean
    Queue mailQueue(){
        return new Queue(mailQueueName);
    }
    @Bean
    public Binding bindingMail(Queue mailQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(mailQueue).to(exchangeAuth).with(mailBindingKey);
    }

    @Bean
    Queue mailPersonnelQueue(){
        return new Queue(mailPersonnelQueueName);
    }

    @Bean
    public Binding bindingPersonnelMail(Queue mailPersonnelQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(mailPersonnelQueue).to(exchangeAuth).with(mailPersonnelBindingKey);
    }

    @Bean
    Queue registerGuestQueue(){
        return new Queue(registerGuestQueueName);
    }
    @Bean
    public Binding bindingRegisterGuest(Queue registerGuestQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerGuestQueue).to(exchangeAuth).with(registerGuestBindingKey);
    }

    @Bean
    Queue registerGuestActivationQueue(){
        return new Queue(registerGuestActivationQueueName);
    }
    @Bean
    public Binding bindingRegisterGuestActivation(Queue registerGuestActivationQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerGuestActivationQueue).to(exchangeAuth).with(registerGuestActivationBindingKey);
    }



    @Bean
    Queue registerSupervisorQueue(){
        return new Queue(registerSupervisorQueueName);
    }
    @Bean
    public Binding bindingRegisterSupervisor(Queue registerSupervisorQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerSupervisorQueue).to(exchangeAuth).with(registerSupervisorBindingKey);
    }

    @Bean
    Queue getCompanyIdForUserLoginQueue(){
        return new Queue(getCompanyIdForUserLoginQueue);
    }
    @Bean
    public Binding bindingCompanyIdForUserLogin(Queue getCompanyIdForUserLoginQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(getCompanyIdForUserLoginQueue).to(exchangeAuth).with(getCompanyIdForUserLoginBindingKey);
    }

    @Bean
    Queue companyStatusCheckQueue(){
        return new Queue(companyStatusCheckQueue);
    }
    @Bean
    public Binding bindingCompanyStatusCheck(Queue companyStatusCheckQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(companyStatusCheckQueue).to(exchangeAuth).with(companyStatusCheckBindingKey);
    }

    @Bean
    Queue searchForCompanyNameQueue(){
        return new Queue(searchForCompanyNameQueue);
    }
    @Bean
    public Binding bindingSearchForCompanyName(Queue searchForCompanyNameQueue, DirectExchange exchangeAuth){
        return BindingBuilder.bind(searchForCompanyNameQueue).to(exchangeAuth).with(searchForCompanyNameBindingKey);
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
