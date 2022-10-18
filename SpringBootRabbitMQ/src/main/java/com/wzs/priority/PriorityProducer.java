package com.wzs.priority;

import com.rabbitmq.client.AMQP;

public class PriorityProducer {
    public static void main(String[] args) {
        Channel channel = Channe
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(10).build();
        for (int i = 0; i <10 ; i++) {

        }
    }
}
