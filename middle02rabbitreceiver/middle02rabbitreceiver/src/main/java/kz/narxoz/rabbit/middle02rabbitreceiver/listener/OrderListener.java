package kz.narxoz.rabbit.middle02rabbitreceiver.listener;

import kz.narxoz.rabbit.middle02rabbitreceiver.dto.OrderDTO;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order_update_queue"),
            exchange = @Exchange(value = "${mq.order.fanout.exchange}",
                    type = ExchangeTypes.FANOUT),
            key = ""

    ))
    public void receiveOrders(OrderDTO orderDTO){
        log.info("Received order message: {}, {}", orderDTO.getMessage(), orderDTO.getStatus());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order_notify_queue"),
            exchange = @Exchange(value = "${mq.order.fanout.exchange}",
            type = ExchangeTypes.FANOUT),
            key = ""
    ))
    public void receiveNotify(OrderDTO orderDTO){
        log.info("Received notify message: {}, {}", orderDTO.getMessage(), orderDTO.getStatus());
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order_process_queue"),
            exchange = @Exchange(value = "${mq.order.fanout.exchange}",
                    type = ExchangeTypes.FANOUT),
            key = ""
    ))
    public void receiveProcess(OrderDTO orderDTO){
        log.info("Received processes message: {}, {}", orderDTO.getMessage(), orderDTO.getStatus());
    }
}
