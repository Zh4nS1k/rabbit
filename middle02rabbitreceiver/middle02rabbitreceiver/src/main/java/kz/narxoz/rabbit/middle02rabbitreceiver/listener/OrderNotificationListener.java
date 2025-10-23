package kz.narxoz.rabbit.middle02rabbitreceiver.listener;

import kz.narxoz.rabbit.middle02rabbitreceiver.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderNotificationListener {

    @RabbitListener(queues = "${mq.order.queue.almaty}")
    public void handleAlmatyOrders(OrderDTO order,
                                   @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        logOrder("Almaty", routingKey, order);
    }

    @RabbitListener(queues = "${mq.order.queue.astana}")
    public void handleAstanaOrders(OrderDTO order,
                                   @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        logOrder("Astana", routingKey, order);
    }

    @RabbitListener(queues = "${mq.order.queue.shymkent}")
    public void handleShymkentOrders(OrderDTO order,
                                     @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        logOrder("Shymkent", routingKey, order);
    }

    @RabbitListener(queues = "${mq.order.queue.default}")
    public void handleAllOrders(OrderDTO order,
                                @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        logOrder("Catch-all", routingKey, order);
    }

    private void logOrder(String queueLabel, String routingKey, OrderDTO order) {
        log.info("Received order [{} | routing={}]: restaurant='{}', courier='{}', foods={}, status={}",
                queueLabel,
                routingKey,
                order.getRestaurant(),
                order.getCourier(),
                order.getFoods(),
                order.getStatus());

        String status = order.getStatus();
        if (status != null && "ERROR".equalsIgnoreCase(status)) {
            log.warn("Simulating failure for order with status ERROR on queue {}", queueLabel);
            throw new IllegalStateException("Processing failed for order with status ERROR");
        }
    }
}
