package com.wzs.rabbitmq.producer;

import com.rabbitmq.client.Channel;

import com.rabbitmq.client.ConfirmCallback;
import com.wzs.rabbitmq.consumer.MessageConsumer;
import com.wzs.rabbitmq.util.ChannenUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

public class ProducerMian {
    private static final int MESSAGE_COUNT = 1000;
    private static Logger log = Logger.getLogger(MessageConsumer.class.getName());

    public static void main(String[] args) throws Exception {
//        Channel channel= ChannenUtil.getChannnel();
//        try {
//            int count =0;
//            while(true) {
//                String message = "你好！欢迎来到work队列！"+count;
//                channel.queueDeclare(ChannenUtil.QUEUE_NAME,false,false,false,null);
//
//                channel.basicPublish("", ChannenUtil.QUEUE_NAME, null, message.getBytes());
//                log.info("消息发送成功！");
//                count++;
//                if(count>5){
//                    break;
//                }
//
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        //ProducerMian.publishMessageIndividually();
//        ProducerMian.publishMessageIndividuallyB();
        ProducerMian.publishMessageIndividually();
        ProducerMian.publishMessageIndividuallyB();
        ProducerMian.publishMessageAsync();
    }

    /**
     * 单个消息确认发布，阻塞式的
     * @throws Exception
     */
    public static void publishMessageIndividually()throws Exception{
        try(Channel channel =ChannenUtil.getChannnel()){
            String queueName= UUID.randomUUID().toString();
            channel.queueDeclare(queueName,false,false,false,null);
            //开启发布确认
            channel.confirmSelect();
            long begin =System.currentTimeMillis();
            for(int i= 0; i < MESSAGE_COUNT;i++){
                String message=i+"条消息";
                channel.basicPublish("",queueName,null,message.getBytes());
                if(channel.waitForConfirms()){
//                    log.info("消息发送成功");
                }

            }
            long end=System.currentTimeMillis();
            log.info("发布"+MESSAGE_COUNT+"个单独确认消息，耗时:"+(end-begin)+"ms");
        }
    }

    /**
     * 多个消息确认发布，阻塞式的
     * @throws Exception
     */
    public static void publishMessageIndividuallyB()throws Exception{
        int basicSize=5;//批量处理数
        int messageCount=0;//统计数
        try(Channel channel =ChannenUtil.getChannnel()){
            String queueName= UUID.randomUUID().toString();
            channel.queueDeclare(queueName,false,false,false,null);
            //开启发布确认
            channel.confirmSelect();
            long begin =System.currentTimeMillis();
            for(int i= 0; i < MESSAGE_COUNT;i++){
                String message=i+"条消息";
                channel.basicPublish("",queueName,null,message.getBytes());
                messageCount++;
                if(messageCount==basicSize){
                    channel.waitForConfirms();
                    messageCount=0;
                }

            }
            //确保没有漏掉为确认的消息
            if(messageCount>0){
                channel.waitForConfirms();
            }
            long end=System.currentTimeMillis();
            log.info("发布"+MESSAGE_COUNT+"个批量确认消息，耗时:"+(end-begin)+"ms");
        }
    }

    /**
     * 异步确认发布
     * @throws Exception
     */
    public static void publishMessageAsync()throws Exception{

        try(Channel channel = ChannenUtil.getChannnel()){
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName,false,false,false,null);
            //开启发布确认
            channel.confirmSelect();

            /**
             * 创建一个线程安全有序的哈希表（高并发场景适用）
             * 1、将序号与消息进行关联
             * 2、通过序列号即可完成批量删除
             * 3、支持并发访问
             */
            ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();
            /**
             * 确认收到消息的回调
             * 1、消息序列号
             * 2、true可以确定小于等于当前的序列号消息
             *    false确认当前序列号
             */
            ConfirmCallback AckCallback = (sequenceNumber, multiple)->{
                if(multiple){
                    //返回小于等于当前序列号的确认消息
                    ConcurrentNavigableMap<Long, String> confirmed=outstandingConfirms.headMap(sequenceNumber,true);
                    //清楚该部分确认消息
                    confirmed.clear();
                }else {
                    //只清除当前序列号的消息
                    outstandingConfirms.remove(sequenceNumber);
                }
            };
            ConfirmCallback nackCallback = (sequenceNumber, multiple)->{
              String message = outstandingConfirms.get(sequenceNumber);
                System.out.println("发布的消息："+message+"未被确认,序列号"+sequenceNumber);
            };
            /**
             * 添加一个异步确认的监听器
             * 1、确认收到消息的回调
             * 2、未收到消息的回调
             */
            channel.addConfirmListener(AckCallback,null);
            long begin = System.currentTimeMillis();
            for(int i = 0; i< MESSAGE_COUNT ; i++){
                String message="条消息"+i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息进行一个关联
                 * 全部是未确认的消息体
                 */
                 outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
                 channel.basicPublish("",queueName,null,message.getBytes());

            }
            long end = System.currentTimeMillis();
            System.out.println("发布"+MESSAGE_COUNT+"个异步确认消息，耗时"+(end-begin)+"ms");


        }

    }



}
