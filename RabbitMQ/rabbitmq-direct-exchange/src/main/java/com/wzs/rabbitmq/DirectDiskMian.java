package com.wzs.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DirectDiskMian {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //设置交换机
        ChannelUtil.setExchangeClass(channel,"direct");
        //创建队列
        String queueName="disk";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定队列
        channel.queueBind(queueName,ChannelUtil.EXCHANGE_NAME,"error");
        //接收消息并准备写入磁盘
        System.out.println("waiting for writing to disk");
        //回调函数
        DeliverCallback deliverCallback = (mTag,message)->{
            File file = new File("D:\\DirectLogs.txt");
            String mess= new String(message.getBody());
            FileUtils.writeStringToFile(file,"消息是："+mess+"绑定的键值是："+message.getEnvelope().getRoutingKey(),"utf-8");
            System.out.println("write successful");
        };
        channel.basicConsume(queueName,true,deliverCallback,tag->{});


    }
}
