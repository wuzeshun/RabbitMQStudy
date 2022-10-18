package com.wzs.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wzs.util.MyChannelUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PriorityProducerMian {
    public static void main(String[] args) throws IOException {
        Channel channel = MyChannelUtil.getChannel();
        //设置队列优先级
        Map<String,Object> argument = new HashMap<>();
        argument.put("x-max-priority",10);
        channel.queueDeclare("wzs",true,false,false,argument);
        //创建消息的priority参数对象
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().priority(10).build();
        //发送消息
        for (int i = 0; i < 10; i++) {
            String message="info"+i;
            if (i == 5) {
                //给第六条消息添加一个更高的优先级
                channel.basicPublish("","wzs",properties,message.getBytes(StandardCharsets.UTF_8));
            }else {
                channel.basicPublish("","wzs",null,message.getBytes(StandardCharsets.UTF_8));
            }
            System.out.println("send message:"+message);
        }

    }
}
