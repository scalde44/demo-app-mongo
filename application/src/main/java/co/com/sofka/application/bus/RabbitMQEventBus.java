package co.com.sofka.application.bus;

import co.com.sofka.application.ApplicationConfig;
import co.com.sofka.application.repo.GsonEventSerializer;
import co.com.sofka.generic.DomainEvent;
import co.com.sofka.generic.EventBus;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQEventBus implements EventBus {


    private final RabbitTemplate rabbitTemplate;
    private final GsonEventSerializer serializer;
    private final RabbitAdmin rabbitAdmin;


    public RabbitMQEventBus(RabbitTemplate rabbitTemplate, GsonEventSerializer serializer, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.serializer = serializer;
        this.rabbitAdmin = rabbitAdmin;
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