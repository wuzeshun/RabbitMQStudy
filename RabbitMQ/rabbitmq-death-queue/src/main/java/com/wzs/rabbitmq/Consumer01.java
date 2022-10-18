package com.wzs.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Consumer01 {

    private static final String NORMAL_EXCHANGE="normal-exchange";
    private  static final String DEATH_EXCHANGE="death-exchange";
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //设置普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置死信交换机
        channel.exchangeDeclare(DEATH_EXCHANGE, BuiltinExchangeType.DIRECT);

        //创建死信队列
        String dqueueName="dqueue";
        channel.queueDeclare(dqueueName,false,false,false,null);
        //绑定死信队列与死信交换机
        channel.queueBind(dqueueName,DEATH_EXCHANGE,"liwei");
        //创建普通队列map参数，目的是为了将无法消费的消息投递到死信队列
        Map<String,Object> argument = new HashMap<>();
        argument.put("x-dead-letter-exchange", DEATH_EXCHANGE);
        argument.put("x-dead-letter-routing-key","liwei");
        //创建普通队列,并设置map参数
        String nqueueName="nqueue";
        channel.queueDeclare(nqueueName,false,false,false,argument);
        //绑定普通队列与普通交换机
        channel.queueBind(nqueueName,NORMAL_EXCHANGE,"wzs");

        //接收消息回调函数
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("Consumer01 receive message is :"+ new String(message.getBody()));
        };
        channel.basicConsume(nqueueName,true,deliverCallback,tag->{});

    }
}
