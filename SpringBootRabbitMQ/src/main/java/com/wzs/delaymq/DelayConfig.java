package com.wzs.delaymq;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WZS
 */
@Configuration
public class DelayConfig {
    private static final String  DELAY_EXCHANGE_NAME="delay.exchange";
    private static final String  DELAY_QUEUE_NAME="delay.queue";
    private static final String  DELAY_QUEUE_ROUTINGKEY="delay.routingkey";

    @Bean("delayqueue")
    public Queue DelayQueue(){
        return new Queue(DELAY_QUEUE_NAME);
    }
    @Bean("delayexchange")
    public CustomExchange DelayExchange(){
        Map<String,Object> args = new HashMap<>(3);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE_NAME,"x-delayed-message",true,false,args);
    }
    @Bean
    public Binding DelayBinding(@Qualifier("delayqueue") Queue delayqueue, @Qualifier("delayexchange") CustomExchange delayexchange){
        return BindingBuilder.bind(delayqueue).to(delayexchange).with(DELAY_QUEUE_ROUTINGKEY).noargs();
    }
}
