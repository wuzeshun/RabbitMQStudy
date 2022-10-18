package com.wzs.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wzs.util.MyChannelUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PriorotyConsumer {

    public static void main(String[] args) throws IOException {
        Channel channel= MyChannelUtil.getChannel();
        //创建普通队列map参数，目的是为了将无法消费的消息投递到死信队列

        //接收消息回调函数
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("Consumer01 receive message is :"+ new String(message.getBody()));
        };
        channel.basicConsume("wzs",true,deliverCallback,tag->{});

    }
}
