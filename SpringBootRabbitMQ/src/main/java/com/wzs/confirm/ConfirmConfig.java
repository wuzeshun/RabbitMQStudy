package com.wzs.confirm;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmConfig {
    private static final String CONFIRM_EXCHANGE="confirm.exchange";
    private static final String CONFIRM_QUEUE="confirm.queue";

    @Bean("confirmqueue")
    public Queue CongirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE).build();

    }

    @Bean("confirmexchange")
    public DirectExchange confirmExchange(){
        return new DirectExchange(CONFIRM_EXCHANGE);
    }

    @Bean
    public Binding QueueBinding(@Qualifier("confirmqueue")Queue queue,@Qualifier("confirmexchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("wzs");
    }
}
