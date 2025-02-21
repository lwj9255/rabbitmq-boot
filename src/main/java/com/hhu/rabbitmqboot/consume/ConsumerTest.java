package com.hhu.rabbitmqboot.consume;

import com.hhu.rabbitmqboot.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsumerTest {
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(String msg, Channel channel, Message message) throws IOException {
        System.out.println("队列的消息为："+msg);
        String correlationId = message.getMessageProperties().getCorrelationId();
        System.out.println("唯一标识为:"+correlationId);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
