package com.wzs.rabbitmq.consumer;


import com.rabbitmq.client.*;

import com.wzs.rabbitmq.util.ChannenUtil;


import java.io.IOException;
import java.util.logging.Logger;


public class MessageConsumer {
    private static  Channel channel= ChannenUtil.getChannnel();
    private static Logger log = Logger.getLogger(MessageConsumer.class.getName());

    public static void main(String[] args) {
        DeliverCallback deliverCallback=(consumerTag,deliver)->{
           String message = new String(deliver.getBody());
            try {

                Thread.sleep(1000);
                log.info(message);
                channel.basicAck(deliver.getEnvelope().getDeliveryTag(),false);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        };

        CancelCallback cancelCallback=(consumerTag)->{
            log.info("消息传递被中断！");
        };
        try {
            //
            channel.basicConsume(ChannenUtil.QUEUE_NAME,false,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
