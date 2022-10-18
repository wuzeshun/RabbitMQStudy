package com.wzs.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DeathProducerMian {
    private static final String NORMAL_EXCHANGE="normal-exchange";

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置TTL时间
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().expiration("5000").build();
        //发送消息
        for (int i = 0; i < 10; i++) {
            String message="info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"wzs",properties,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("send message:"+message);
        }

    }
}
