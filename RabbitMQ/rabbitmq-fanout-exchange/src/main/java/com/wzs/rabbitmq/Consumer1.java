package com.wzs.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author WZS
 */
public class Consumer1 {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtil.getChannel();
        //设置交换机
        channel.exchangeDeclare(ChannelUtil.EXCHANGE_NAME,"fanout");
        //创建临时队列，
        String queueName = channel.queueDeclare().getQueue();
        //绑定队列
        channel.queueBind(queueName,ChannelUtil.EXCHANGE_NAME,"");
        //准备接收消息并打印消息
        DeliverCallback deliverCallback = (message1,message2)->{
            System.out.println("广播消息是："+new String(message2.getBody()));
        };
        //接收消息
        channel.basicConsume(queueName,true,deliverCallback, message->{});
    }


}


