package com.example.backexchange.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String reason) {
        String id=correlationData!=null?correlationData.getId():null;
        if(ack){
            log.info("交换机接收到的消息的id是{}",id);
        }else {
            log.error("交换机为接收到的消息id是{}，原因是{}",id,reason);
        }
    }
}
