package pl.sodexo.it.gryf.service.local.impl.publicbenefits.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstanceEventRepository;
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

    @Autowired
    private PbeProductInstanceEventRepository pbeProductInstanceEventRepository;

    //PUBLIC METHODS - CREATE EVENTS

    public PbeProductInstanceEvent saveEvent(PbeProductInstance instance, String typeId, Object sourceId){
        return saveEvent(instance, productInstanceEventTypeRepository.get(typeId), sourceId);
    }

    public PbeProductInstanceEvent saveEvent(PbeProductInstance instance, PbeProductInstanceEventType type, Object sourceId){
        PbeProductInstanceEvent event = new PbeProductInstanceEvent();
        event.setProductInstance(instance);
        event.setType(type);
        event.setSourceId(sourceId != null ? sourceId.toString() : null);
        return pbeProductInstanceEventRepository.save(event);
    }

}
