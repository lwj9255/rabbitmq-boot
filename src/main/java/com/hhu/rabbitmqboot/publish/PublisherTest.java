package com.hhu.rabbitmqboot.publish;

import com.hhu.rabbitmqboot.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class PublisherTest {
    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Test
    public void publish(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,"big.black.dog","message");
        System.out.println("消息发送成功！");
    }

    @Test
    public void publishWithProps(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "big.black.dog", "messageWithProps",
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setCorrelationId("456");
                        return message;
                    }
                });
        System.out.println("附带特性的消息发送成功！");
    }

    @Test
    public void publishWithConfirms() throws IOException {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if(ack){
                    System.out.println("消息已经送达到交换机");
                }else{
                    System.out.println("消息没有送到到exchange");
                }
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,"big.black.dog","message");
        System.out.println("消息发送成功！");

        System.in.read();
    }

    @Test
    public void publishWithReturns() throws IOException {
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                String msg = new String(returned.getMessage().getBody());
                System.out.println("消息："+msg+"发送失败！请补救！");
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,"big.white.dog","message");
        System.out.println("消息发送成功！");

        System.in.read();
    }

    @Test
    public void publishWithBasicProperties() throws IOException {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "big.white.dog", "message",
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        // 设置消息的持久化
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    }
                });
        System.out.println("消息发送成功！");

        System.in.read();
    }
}
