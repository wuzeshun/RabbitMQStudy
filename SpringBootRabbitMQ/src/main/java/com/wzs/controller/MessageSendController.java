package com.wzs.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class MessageSendController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("send/{message}")
    public void SendMessage(@PathVariable("message")String message){
        log.info("时间：{}，发送一条消息给两个队列，消息为：{}",new Date(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列，消息体为:"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列，消息体为："+message);
    }

    @GetMapping("send/{message}/{ttl}")
    public void SendMessage1(@PathVariable("message")String message,@PathVariable("ttl")String timeTtl){
        log.info("时间：{}，发送一条消息给两个队列，消息为：{}",new Date(),message);
        rabbitTemplate.convertAndSend("X","XC","消息体为:"+message,correlationData->{
            correlationData.getMessageProperties().setExpiration(timeTtl);
            return correlationData;
        });

    }

    @GetMapping("send/delay/{message}/{ttl}")
    public  void DelayMessageSend(@PathVariable("message")String message,@PathVariable("ttl")String ttl){

        rabbitTemplate.convertAndSend("delay.exchange","delay.routingkey","产生的消息是："+message,CorrelationData->{
            CorrelationData.getMessageProperties().setDelay(Integer.valueOf(ttl));
            return CorrelationData;

        });

        System.out.println("延时时间为："+ttl+"ms消息message："+message+"时间："+ new Date());
    }
}
