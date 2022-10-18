package com.wzs.rabbitmq;

        import com.rabbitmq.client.BuiltinExchangeType;
        import com.rabbitmq.client.Channel;
        import com.rabbitmq.client.DeliverCallback;

        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;

/**
 * @author WZS
 */
public class ConsumerMian01 {
    private static  final String N_EXCHANGE="normal-exchange";
    private static  final String D_EXCHANGE="dead-exchange";

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtil.getChannel();
        //设置普通交换机
        channel.exchangeDeclare(N_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置死信交换机
        channel.exchangeDeclare(D_EXCHANGE,BuiltinExchangeType.DIRECT);
        //创建死信队列
        String D_queue="dead";
        channel.queueDeclare(D_queue,false,false,false,null);
        //将死信队列与死信交换机进行绑定
        channel.queueBind(D_queue,D_EXCHANGE,"li");
        //设置普通队列并创建map参数，设置最大消息长度
        Map<String,Object> argument = new HashMap<>();
        argument.put("x-dead-letter-exchange",D_EXCHANGE);
        argument.put("x-dead-letter-routing-key","li");
        String N_queue="queue";
        channel.queueDeclare(N_queue,false,false,false,argument);
        //绑定队列
        channel.queueBind(N_queue,N_EXCHANGE,"wzs");

        System.out.println("waiting receive message");
        DeliverCallback deliverCallback = (consumerTag,message)->{
            String mag= new String(message.getBody());
            //检测拒收消息
            if(mag.equals("info5")||mag.equals("info8")||mag.equals("info9")){
                System.out.println("CMER01 refuse receive message is:"+mag);
                //设置false不允许重新排入队列，若存在死信队列就将该消息推入死信队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                //正常放行消息
                System.out.println("CMER01 receive message is:"+mag);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }

        };
        boolean AutoAck=false;
        channel.basicConsume(N_queue,AutoAck,deliverCallback,Tag->{});
    }
}
