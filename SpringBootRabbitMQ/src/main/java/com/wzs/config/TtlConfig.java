package com.wzs.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TtlConfig {
    private static final  String XCHANGE_NAME="X";
    private static final String YCHANGE_NAME="Y";
    private static final String QUEUE_A="QA";
    private static final String QUEUE_B="QB";
    private static final String QUEUE_C="QC";
    private static final String QUEUE_D="QD";

    //配置普通交换机
    @Bean("xchange")
    public DirectExchange xChange(){
        return new DirectExchange(XCHANGE_NAME);
    }
    //搭建死信交换机
    @Bean("ychange")
    public DirectExchange yChange(){
        return new DirectExchange(YCHANGE_NAME);
    }
    //创建死信队列
    @Bean("queueD")
    public Queue QueueD(){
        return new Queue(QUEUE_D);
    }

    //创建普队列
    @Bean("queueA")
    public Queue QueueA(){
        Map<String, Object> argument = new HashMap<>(6);
        argument.put("x-dead-letter-exchange",YCHANGE_NAME);
        argument.put("x-dead-letter-routing-key","YD");
        argument.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(argument).build();
    }

    //创建普队列
    @Bean("queueB")
    public Queue QueueB(){
        Map<String, Object> argument = new HashMap<>(6);
        argument.put("x-dead-letter-exchange",YCHANGE_NAME);
        argument.put("x-dead-letter-routing-key","YD");
        argument.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(argument).build();
    }


    //普通队列绑定交换机
    @Bean
    public Binding QueueABinding(@Qualifier("queueA")Queue queueA,@Qualifier("xchange")DirectExchange xchange){
        return BindingBuilder.bind(queueA).to(xchange).with("XA");
    }
    @Bean
    public Binding QueueB_Binding(@Qualifier("queueB")Queue queueB,@Qualifier("xchange")DirectExchange xchange){
        return BindingBuilder.bind(queueB).to(xchange).with("XB");
    }
    //死信队列绑定死信交换机
    @Bean
    public Binding QueueDBinding(@Qualifier("queueD")Queue queueD,@Qualifier("ychange")DirectExchange ychange){

        return BindingBuilder.bind(queueD).to(ychange).with("YD");
    }
    //优化的延迟队列
    @Bean("queueC")
    public Queue queueC(){
        Map<String,Object> argument = new HashMap<>(6);
        argument.put("x-dead-letter-exchange","Y");
        argument.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(argument).build();
    }
    @Bean
    public Binding QueueCBinding(@Qualifier("queueC")Queue queueC,@Qualifier("xchange")DirectExchange xchange){
        return BindingBuilder.bind(queueC).to(xchange).with("XC");
    }
}
