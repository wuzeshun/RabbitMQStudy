package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MessageSendMian {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //创建消息map
        HashMap<String,String> messageMap = new HashMap<>();
        messageMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        messageMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        messageMap.put("quick.orange.fox","被队列 Q1 接收到");
        messageMap.put("lazy.brown.fox","被队列 Q2 接收到");
        messageMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        messageMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        messageMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        messageMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
        //消息发送
        for(String key: messageMap.keySet()){
            String message = messageMap.get(key);
            channel.basicPublish(ChannelUtil.EXCHANGE_NAME,key,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("send message is:"+message+" \t the routing_key is:"+key);
        }

    }
}
