package com.wzs.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


@Component
@Slf4j
public class ConfirmConsumer {
    @RabbitListener(queues="confirm.queue")
    public void RecevieMessage(Message message) throws UnsupportedEncodingException {
        String msg= new String(message.getBody(),"utf-8");
        log.info("receive message is "+msg);
    }
}
