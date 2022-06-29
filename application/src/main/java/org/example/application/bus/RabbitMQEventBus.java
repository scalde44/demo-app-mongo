package org.example.application.bus;

import org.example.application.ApplicationConfig;
import org.example.application.repo.GsonEventSerializer;
import org.example.generic.DomainEvent;
import org.example.generic.EventBus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQEventBus implements EventBus {


    private final RabbitTemplate rabbitTemplate;
    private final GsonEventSerializer serializer;


    public RabbitMQEventBus(RabbitTemplate rabbitTemplate, GsonEventSerializer serializer) {
        this.rabbitTemplate = rabbitTemplate;
        this.serializer = serializer;
    }

    @Override
    public void publish(DomainEvent event) {
        var notification = new Notification(
                event.getClass().getCanonicalName(),
                serializer.serialize(event));
        rabbitTemplate.convertAndSend(ApplicationConfig.EXCHANGE, event.getType(), notification.serialize().getBytes());
    }

    @Override
    public void publishError(Throwable errorEvent) {

    }


}