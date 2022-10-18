package com.wzs.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReplyConfig implements RabbitTemplate.ReturnCallback{

    @Override
    public void returnedMessage(Message message, int replyCode, String reply_reason, String exchange, String routingKey) {
        log.error("消息{}，被交换机{}退回,退回原因是{},路由Key是{}",new String(message.getBody()),exchange,reply_reason,routingKey);
    }
}
