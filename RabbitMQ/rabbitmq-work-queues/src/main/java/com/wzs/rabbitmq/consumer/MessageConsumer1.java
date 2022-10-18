package com.wzs.rabbitmq.consumer;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wzs.rabbitmq.util.ChannenUtil;



import java.io.IOException;
import java.util.logging.Logger;

public class MessageConsumer1 {
    private static Channel channel= ChannenUtil.getChannnel();
    private static  final Logger log = Logger.getLogger(MessageConsumer1.class.getName());

    public static void main(String[] args) throws IOException {

        DeliverCallback deliverCallback=(consumerTag,deliver)->{
            String message = new String(deliver.getBody());
            try {
                Thread.sleep(30000);
                log.info("consumer1在消费:"+message);
                channel.basicAck(deliver.getEnvelope().getDeliveryTag(),false);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        };

        CancelCallback cancelCallback=(consumerTag)->{
            log.info("消息传递中断！");
        };
        //设置收到提交，FALSE
        channel.basicConsume(ChannenUtil.QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
