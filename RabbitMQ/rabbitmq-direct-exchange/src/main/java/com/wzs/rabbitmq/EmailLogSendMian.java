package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WZS
 */
public class EmailLogSendMian {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //创建一个消息mapper
        Map<String,String> meaasgeMap = new HashMap<>();
        meaasgeMap.put("error","this is log of error about system");
        meaasgeMap.put("waring","this message about waring");
        meaasgeMap.put("info","simple info");
        meaasgeMap.put("debug","debug info");

        //发送消息
        for(String k:meaasgeMap.keySet()){
            String message= meaasgeMap.get(k);
            channel.basicPublish(ChannelUtil.EXCHANGE_NAME,k,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("sending message is:"+message);
        }
    }


}
