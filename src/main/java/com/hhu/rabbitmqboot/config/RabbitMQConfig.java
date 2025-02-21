package com.hhu.rabbitmqboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "boot-exchange";
    public static final String QUEUE = "boot-queue";
    public static final String ROUTING_KEY = "*.black.*";

    @Bean
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE).build();
    }

    @Bean
    public Queue bootQueue(){
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding bootBinding(Exchange bootExchange,Queue bootQueue){
        return BindingBuilder.bind(bootQueue).to(bootExchange).with(ROUTING_KEY).noargs();
    }
}
