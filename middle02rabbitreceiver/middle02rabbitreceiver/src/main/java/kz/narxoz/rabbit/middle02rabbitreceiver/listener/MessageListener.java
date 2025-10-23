package kz.narxoz.rabbit.middle02rabbitreceiver.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "message-exchange",
                    type = ExchangeTypes.DIRECT),
            value = @Queue(value = "message-queue"), key = "key123"))
    public void receiveMessage(String message){
        log.info("Received message: {}", message);
    }
}
