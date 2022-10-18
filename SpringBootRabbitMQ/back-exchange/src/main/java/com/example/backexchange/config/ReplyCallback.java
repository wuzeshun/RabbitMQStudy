package com.example.backexchange.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReplyCallback implements RabbitTemplate.ReturnCallback {
    @SneakyThrows
    @Override
    public void returnedMessage(Message message, int replyCode, String replyReason, String exchange, String routingKey) {
       log.info("不能被交换机{} 路由的消息是{} 消息的路由key是{}，被回退的原因是{}",exchange,new String(message.getBody(),"utf-8"),routingKey,replyReason);
    }
}
