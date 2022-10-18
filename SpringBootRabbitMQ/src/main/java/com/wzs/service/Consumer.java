package com.wzs.service;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
@Slf4j
public class Consumer {
    @RabbitListener(queues = "QD")
    public void reciveD(Message message, Channel channel) throws UnsupportedEncodingException {
        String msg= new String(message.getBody(),"utf-8");
        log.info("时间：{},收到死信队列的消息为:{}",new Date(),msg);

    }

    @RabbitListener(queues = "delay.queue")
    public void reciveDelayMessage(Message message) throws UnsupportedEncodingException {
        String msg= new String(message.getBody(),"utf-8");
        log.info("时间：{},收到延迟队列的消息为:{}",new Date().toString(),msg);

    }
}
