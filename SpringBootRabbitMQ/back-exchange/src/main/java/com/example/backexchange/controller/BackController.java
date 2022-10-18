package com.example.backexchange.controller;

import com.example.backexchange.config.ConfirmCallback;
import com.example.backexchange.config.ReplyCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@Slf4j
public class BackController {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ConfirmCallback confirmCallback;
    @Autowired
    ReplyCallback replyCallback;
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(confirmCallback);
    }
    @GetMapping("back/send/{message}")
    public void sendMessageCirm(@PathVariable("message")String message){
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend("normal.exchange","wzs",message+"wzs",correlationData1);
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend("normal.exchange","Li",message+"Li",correlationData2);
        log.info("producer send message is :"+message);
    }
}
