package com.example.backexchange.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BackConfig {
    private static final String EXCHANGE_NAME="normal.exchange";
    private static final String QUEUE_NAME="normal.queue";
    private static final String BACKEXCHANGE_NAME="back.exchange";
    private static final String BACKQUEUE="back.queue";
    private static final String BACKQUEUE_WARRING="back.warring_queue";
    //创建普通队列
    @Bean("normal.queue")
    public Queue NormalQueue(){
        return  QueueBuilder.durable(QUEUE_NAME).build();
    }
    //创建普通交换机,并设置备份交换机
    @Bean("normal.exchange")
    public DirectExchange NormalExchange(){
        ExchangeBuilder exchange = ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).withArgument("alternate-exchange",BACKEXCHANGE_NAME);
        return (DirectExchange) exchange.build();
    }
    //绑定交换机
    @Bean
    public Binding queueBinding(@Qualifier("normal.queue")Queue queue,@Qualifier("normal.exchange")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("wzs");
    }

    //声明备份队列
    @Bean("back.queue")
    public Queue BackQueue(){
        return QueueBuilder.durable(BACKQUEUE).build();
    }
    //声明警告队列
    @Bean("back.warring_queue")
    public Queue BackQueueWarring(){
        return QueueBuilder.durable(BACKQUEUE_WARRING).build();
    }
    //创建备份交换机
    @Bean("back.exchange")
    public FanoutExchange BackExchange(){
        return ExchangeBuilder.fanoutExchange(BACKEXCHANGE_NAME).build();
    }
    //绑定备份队列，备份交换机
    @Bean
    public Binding BackBinding(@Qualifier("back.queue")Queue queue,@Qualifier("back.exchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    //绑定备份队列，备份交换机
    @Bean
    public Binding BackBindingWarring(@Qualifier("back.warring_queue")Queue queueWarring,@Qualifier("back.exchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queueWarring).to(fanoutExchange);
    }
}
