package pl.sodexo.it.gryf.service.local.impl.publicbenefits.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstancePoolEventRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstancePoolEventTypeRepository;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.*;

/**
 * Created by Isolution on 2016-11-15.
 */
@Component
public class PbeProductInstancePoolEventBuilder {

    //PRIVATE FIELDS

    @Autowired
    private PbeProductInstancePoolEventTypeRepository productInstancePoolEventTypeRepository;

    @Autowired
    private PbeProductInstancePoolEventRepository pbeProductInstancePoolEventRepository;

    //PUBLIC METHODS - CREATE EVENTS

    public PbeProductInstancePoolEvent saveEvent(PbeProductInstancePool pool, String typeId, Long sourceId, Integer num){
        return saveEvent(pool, productInstancePoolEventTypeRepository.get(typeId), sourceId, num);
    }

    public PbeProductInstancePoolEvent saveEvent(PbeProductInstancePool pool, PbeProductInstancePoolEventType type, Long sourceId, Integer num){
        PbeProductInstancePoolEvent event = new PbeProductInstancePoolEvent();
        event.setProductInstancePool(pool);
        event.setType(type);
        event.setSourceId(sourceId);
        event.setNum(num);
        return pbeProductInstancePoolEventRepository.save(event);
    }

}
