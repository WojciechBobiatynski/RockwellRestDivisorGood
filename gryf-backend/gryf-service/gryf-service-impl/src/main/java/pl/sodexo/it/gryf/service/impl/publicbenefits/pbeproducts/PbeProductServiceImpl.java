package pl.sodexo.it.gryf.service.impl.publicbenefits.pbeproducts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.*;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstanceEvent;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePoolEvent;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePoolUse;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproducts.PbeProductService;

/**
 * Created by Isolution on 2016-11-07.
 */
@Service
@Transactional
public class PbeProductServiceImpl implements PbeProductService {

    @Autowired
    private PbeProductRepository productRepository;

    @Autowired
    private PbeProductEmissionRepository productEmissionRepository;

    @Autowired
    private PbeProductInstanceRepository productInstanceRepository;

    @Autowired
    private PbeProductInstanceEventRepository productInstanceEventRepository;

    @Autowired
    private PbeProductInstanceEventTypeRepository productInstanceEventTypeRepository;

    @Autowired
    private PbeProductInstancePoolRepository productInstancePoolRepository;

    @Autowired
    private PbeProductInstancePoolEventRepository productInstancePoolEventRepository;

    @Autowired
    private PbeProductInstancePoolEventTypeRepository productInstancePoolEventTypeRepository;

    @Autowired
    private PbeProductInstanceStatusRepository productInstanceStatusRepository;

    @Autowired
    private TrainingInstanceStatusRepository trainingInstanceStatusRepository;

    @Autowired
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    private PbeProductInstancePoolUseRepository productInstancePoolUseRepository;

    @Override
    public void test() {

        System.out.println("-----------");
        PbeProductInstanceEvent productInstanceEvent = productInstanceEventRepository.get(1L);
        System.out.println("productInstanceEvent=" + productInstanceEvent);
        System.out.println("productInstanceEvent.getProductInstance()=" + productInstanceEvent.getProductInstance());
        System.out.println("productInstanceEvent.getType()=" + productInstanceEvent.getType());
        System.out.println("productInstanceEvent.getProductInstance().getProductEmission()=" + productInstanceEvent.getProductInstance().getProductEmission());
        System.out.println("productInstanceEvent.getProductInstance().getProductEmission().getProduct()=" + productInstanceEvent.getProductInstance().getProductEmission().getProduct());

        System.out.println("-----------");
        PbeProductInstancePoolEvent productInstancePoolEvent = productInstancePoolEventRepository.get(22L);
        System.out.println("productInstancePoolEvent=" + productInstancePoolEvent);
        System.out.println("productInstancePoolEvent.getProductInstancePool()=" + productInstancePoolEvent.getProductInstancePool());

        System.out.println("-----------");
        TrainingInstance trainingInstance = trainingInstanceRepository.get(1L);
        System.out.println("trainingInstance=" + trainingInstance);
        System.out.println("trainingInstance.getTraining()=" + trainingInstance.getTraining());
        System.out.println("trainingInstance.getIndividual()=" + trainingInstance.getIndividual());

        System.out.println("-----------");
        PbeProductInstancePoolUse productInstancePoolUse = productInstancePoolUseRepository.get(10L);
        System.out.println("productInstancePoolUse=" + productInstancePoolUse);
        System.out.println("productInstancePoolUse.getProductInstancePool()=" + productInstancePoolUse.getProductInstancePool());
    }
}
