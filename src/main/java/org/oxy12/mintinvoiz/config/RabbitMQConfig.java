package org.oxy12.mintinvoiz.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String AUDIT_LOG_QUEUE = "audit.log.queue";
    public static final String AUDIT_LOG_EXCHANGE = "audit.log.exchange";
    public static final String AUDIT_LOG_ROUTING_KEY = "audit.log";

    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_ROUTING_KEY = "email.send";

    public static final String SMS_QUEUE = "sms.queue";
    public static final String SMS_EXCHANGE = "sms.exchange";
    public static final String SMS_ROUTING_KEY = "sms.send";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    // Audit Log Queue Configuration
    @Bean
    public Queue auditLogQueue() {
        return QueueBuilder.durable(AUDIT_LOG_QUEUE).build();
    }

    @Bean
    public DirectExchange auditLogExchange() {
        return new DirectExchange(AUDIT_LOG_EXCHANGE);
    }

    @Bean
    public Binding auditLogBinding() {
        return BindingBuilder.bind(auditLogQueue())
                .to(auditLogExchange())
                .with(AUDIT_LOG_ROUTING_KEY);
    }

    // Email Queue Configuration
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    // SMS Queue Configuration
    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(SMS_QUEUE).build();
    }

    @Bean
    public DirectExchange smsExchange() {
        return new DirectExchange(SMS_EXCHANGE);
    }

    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(smsQueue())
                .to(smsExchange())
                .with(SMS_ROUTING_KEY);
    }
}