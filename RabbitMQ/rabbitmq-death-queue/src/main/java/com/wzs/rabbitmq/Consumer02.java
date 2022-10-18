package com.wzs.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class Consumer02 {

    private  static final String DEAD_EXCHANGE="death-exchange";
    public static void main(String[] argv) throws Exception {
            Channel channel = ChannelUtil.getChannel();
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            String deadQueue = "dqueue";
            channel.queueDeclare(deadQueue, false, false, false, null);
            channel.queueBind(deadQueue, DEAD_EXCHANGE, "death");
            System.out.println("等待接收死信队列消息.....");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Consumer02 接收死信队列的消息" + message);
            };
            channel.basicConsume(deadQueue, true, deliverCallback, consumerTag -> {
            });

    }
}
