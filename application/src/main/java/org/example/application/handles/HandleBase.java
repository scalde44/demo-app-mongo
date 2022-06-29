package org.example.application.handles;

import org.example.generic.DomainEvent;
import org.example.generic.EventBus;
import org.example.generic.EventStoreRepository;
import org.example.generic.StoredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HandleBase {
    @Autowired
    private EventStoreRepository repository;
    @Autowired
    private StoredEvent.EventSerializer eventSerializer;
    @Autowired
    private EventBus eventBus;

    public void emit(List<DomainEvent> events) {
        events.forEach(domainEvent -> {
            var stored = StoredEvent.wrapEvent(domainEvent, eventSerializer);
            repository.saveEvent("program", domainEvent.getAggregateId(), stored);
            eventBus.publish(domainEvent);
        });
    }
}
