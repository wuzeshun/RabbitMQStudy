package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class SecondQueueMian {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //设置交换机
        ChannelUtil.setExchangeClass(channel,"topic");
        //创建队列
        String queueName = "secondQueue";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定队列
        channel.queueBind(queueName,ChannelUtil.EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,ChannelUtil.EXCHANGE_NAME,"lazy.#");
        //等待接收消息并打印消息
        DeliverCallback deliverCallback = (mtag,message)->{
            System.out.println("the message is :"+new String (message.getBody())+"\t message key is:"+message.getEnvelope().getRoutingKey());
        };
        //接收消息
        channel.basicConsume(queueName,true,deliverCallback,tag->{});
    }
}
