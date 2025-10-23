package kz.narxoz.rabbit.middle02rabbit.service;

import kz.narxoz.rabbit.middle02rabbit.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange orderTopicExchange;

    public void sendOrderToRegion(OrderDTO orderDTO, String region) {
        if (!StringUtils.hasText(region)) {
            throw new IllegalArgumentException("Region must be provided");
        }

        String normalizedRegion = region.trim().toLowerCase(Locale.ROOT);
        String routingKey = "order." + normalizedRegion;
        rabbitTemplate.convertAndSend(orderTopicExchange.getName(), routingKey, orderDTO);
        log.info("Order sent to region {} with routing key {}", normalizedRegion, routingKey);
    }
}
