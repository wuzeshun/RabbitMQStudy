package com.wzs.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String id=correlationData !=null?correlationData.getId():"";
        if(ack){
            log.info("exchange receive message id is {}",id);
        }
        else
        {
            log.info("exchange no receive message id is {},the reason is {}",id,s);
        }

    }

}
