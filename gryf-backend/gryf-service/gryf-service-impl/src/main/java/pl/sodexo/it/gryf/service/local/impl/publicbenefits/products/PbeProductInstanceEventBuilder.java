package pl.sodexo.it.gryf.service.local.impl.publicbenefits.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstanceEventTypeRepository;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.*;

/**
 * Created by Isolution on 2016-11-15.
 */
@Component
public class PbeProductInstanceEventBuilder {

    //PRIVATE FIELDS

    @Autowired
    private PbeProductInstanceEventTypeRepository productInstanceEventTypeRepository;

    //PUBLIC METHODS - CREATE EVENTS

    public PbeProductInstanceEvent createPbeProductInstanceEvent(PbeProductInstance instance, String typeId, Long sourceId){
        return createPbeProductInstanceEvent(instance, productInstanceEventTypeRepository.get(typeId), sourceId);
    }

    public PbeProductInstanceEvent createPbeProductInstanceEvent(PbeProductInstance instance, PbeProductInstanceEventType type, Long sourceId){
        PbeProductInstanceEvent event = new PbeProductInstanceEvent();
        event.setProductInstance(instance);
        event.setType(type);
        event.setSourceId(sourceId);
        return event;
    }

}
