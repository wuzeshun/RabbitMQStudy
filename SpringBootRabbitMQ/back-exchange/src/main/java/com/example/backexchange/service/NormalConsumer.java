package com.example.backexchange.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class NormalConsumer {
    @RabbitListener(queues="normal.queue")
    public void RecevieMessage(Message message) throws UnsupportedEncodingException {
        log.info("正常接收到的消息是{}",new String(message.getBody(),"utf-8"));
    }
}
