package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ProducerMian {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //发送消息
        for (int i = 0; i < 11; i++) {
            String mag="information"+i+"你好";
            channel.basicPublish("normal-exchange","wzs",null,mag.getBytes(StandardCharsets.UTF_8));
            System.out.println("send message is:"+mag);
        }
    }
}
