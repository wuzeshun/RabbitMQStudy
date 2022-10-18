package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;

public class  ConsumerMian02 {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //读取死信队列中的消息
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("CONMER02 receive dead queue message is:" + new String(message.getBody()));
        };
        channel.basicConsume("dead", true, deliverCallback, tag -> {});
    }
}

