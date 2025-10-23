package kz.narxoz.rabbit.middle02rabbitreceiver.config;

import kz.narxoz.rabbit.middle02rabbitreceiver.dto.OrderDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange orderTopicExchange(@Value("${mq.order.topic.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue almatyQueue(@Value("${mq.order.queue.almaty}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue astanaQueue(@Value("${mq.order.queue.astana}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue shymkentQueue(@Value("${mq.order.queue.shymkent}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue defaultRegionQueue(@Value("${mq.order.queue.default}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding almatyBinding(@Qualifier("almatyQueue") Queue queue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(queue).to(orderTopicExchange).with("order.almaty");
    }

    @Bean
    public Binding astanaBinding(@Qualifier("astanaQueue") Queue queue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(queue).to(orderTopicExchange).with("order.astana");
    }

    @Bean
    public Binding shymkentBinding(@Qualifier("shymkentQueue") Queue queue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(queue).to(orderTopicExchange).with("order.shymkent");
    }

    @Bean
    public Binding catchAllBinding(@Qualifier("defaultRegionQueue") Queue queue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(queue).to(orderTopicExchange).with("order.#");
    }

    @Bean
    public Jackson2JsonMessageConverter consumerJackson2MessageConverter(DefaultClassMapper classMapper) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(classMapper);
        return messageConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("kz.narxoz.rabbit.middle02rabbit.dto.OrderDTO", OrderDTO.class);
        idClassMapping.put(OrderDTO.class.getName(), OrderDTO.class);
        classMapper.setIdClassMapping(idClassMapping);
        classMapper.setTrustedPackages("kz.narxoz.rabbit.middle02rabbit.dto",
                "kz.narxoz.rabbit.middle02rabbitreceiver.dto");
        classMapper.setDefaultType(OrderDTO.class);
        return classMapper;
    }
}
