package com.wzs.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@Slf4j
public class ConfirmProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ConfirmCallback confirmCallback;

    @Autowired
    ReplyConfig replyConfig;
    //依赖注入 rabbitTemplate 之后再设置它的回调对象
    @PostConstruct
    public void init(){
        //设置确认回调函数
        rabbitTemplate.setConfirmCallback(confirmCallback);

        /**
         * true：
         *   交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         *  如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        //设置回退回调函数
        rabbitTemplate.setReturnCallback(replyConfig);
    }


    @GetMapping("cirm/send/{message}")
    public void sendMessageCirm(@PathVariable("message")String message){
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend("confirm.exchange","wzs",message+"wzs",correlationData1);
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend("confirm.exchange","Li",message+"Li",correlationData2);
        log.info("producer send message is :"+message);
    }
    
}
