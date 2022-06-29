package org.example.application.repo;

import com.google.gson.Gson;
import org.example.generic.DomainEvent;
import org.example.generic.StoredEvent;
import org.springframework.stereotype.Component;

@Component
public class GsonEventSerializer implements StoredEvent.EventSerializer {
    @Override
    public <T extends DomainEvent> T deserialize(String aSerialization, Class<?> aType) {
        return (T) new Gson().fromJson(aSerialization, aType);
    }

    @Override
    public String serialize(DomainEvent object) {
        return new Gson().toJson(object, object.getClass());
    }
}
