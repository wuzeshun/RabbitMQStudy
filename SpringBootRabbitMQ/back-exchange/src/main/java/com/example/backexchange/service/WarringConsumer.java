package com.example.backexchange.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class WarringConsumer {
    @RabbitListener(queues="back.warring_queue")
    public void ReceiveWarringQueueMessage(Message message) throws UnsupportedEncodingException {
        log.info("接警不可被路由的消息是{}",new String(message.getBody(),"utf-8"));
    }
}
