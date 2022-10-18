package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class FirstQueueMain {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //设置交换机
        ChannelUtil.setExchangeClass(channel,"topic");
        //创建队列
        String queueName="FirstQueue";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定交换机
        channel.queueBind(queueName,ChannelUtil.EXCHANGE_NAME,"*.orange.*");
        //准备接收消息
        DeliverCallback deliverCallback = (mTag,message)->{
            System.out.println("the message is"+new String(message.getBody())+"\t the queue_bind key is:"+message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName,true,deliverCallback,tag->{});

    }
}
