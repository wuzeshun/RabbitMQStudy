package com.wzs.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class DirectConsoleMian {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //设置交换机
        channel.exchangeDeclare(ChannelUtil.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建队列
        String queueName="console";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,ChannelUtil.EXCHANGE_NAME,"waring");
        channel.queueBind(queueName,ChannelUtil.EXCHANGE_NAME,"info");

        //准备接收消息并在控制台打印
        System.out.println("waiting the message from producer");
        //回调函数
        DeliverCallback deliverCallback = (mTag,message)->{
            System.out.println("the key is :"+message.getEnvelope().getRoutingKey()+" \t the message is:"+new String(message.getBody()));
        };
        //接收消息
        channel.basicConsume(queueName,true,deliverCallback,tag->{});
    }
}
