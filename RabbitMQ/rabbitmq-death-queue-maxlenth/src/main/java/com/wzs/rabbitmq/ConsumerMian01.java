package com.wzs.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsumerMian01 {
    private static  final String N_EXCHANGE="normal-exchange";
    private static  final String D_EXCHANGE="dead-exchange";

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //设置普通交换机
        channel.exchangeDeclare(N_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置死信交换机
        channel.exchangeDeclare(D_EXCHANGE,BuiltinExchangeType.DIRECT);
        //创建死信队列
        String D_queue="dead";
        channel.queueDeclare(D_queue,false,false,false,null);
        //将死信队列与死信交换机进行绑定
        channel.queueBind(D_queue,D_EXCHANGE,"li");
        //设置普通队列并创建map参数，设置最大消息长度
        Map<String,Object> argument = new HashMap<>();
        argument.put("x-dead-letter-exchange",D_EXCHANGE);
        argument.put("x-dead-letter-routing-key","li");
        argument.put("x-max-length",6);
        String N_queue="queue";
        channel.queueDeclare(N_queue,false,false,false,argument);
        //绑定队列
        channel.queueBind(N_queue,N_EXCHANGE,"wzs");

        System.out.println("waiting receive message");
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("CMER01 receive message is:"+new String(message.getBody()));
        };
        channel.basicConsume(N_queue,true,deliverCallback,Tag->{});
    }
}
