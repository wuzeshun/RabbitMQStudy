package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;


import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class EmailLogsMian {

        public static void main(String[] args) throws Exception {
            Channel channel = ChannelUtil.getChannel();
            Scanner sc = new Scanner(System.in);

            while(sc.hasNext()){
                String message = sc.next();
                channel.basicPublish(ChannelUtil.EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
                System.out.println("生产者发出消息：" + message);
            }
        }



}
