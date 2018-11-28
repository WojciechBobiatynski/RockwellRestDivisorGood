package pl.sodexo.it.gryf.service.impl.trainingcategory;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingcategory.TrainingCategoryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.api.trainingCategory.TrainingCategoryService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.trainingcategory.TrainingCategoryEntityMapper;

@Service
@Transactional
public class TrainingCategoryServiceImpl implements TrainingCategoryService {

   @Autowired
   private TrainingCategoryEntityMapper trainingCategoryEntityMapper;

   @Autowired
   private TrainingCategoryRepository trainingCategoryRepository;

    @Override
    @Cacheable(cacheName = "trainingCategoryService.findByGrantProgramAndName")
    public TrainingCategoryDTO findByGrantProgramAndName(Long grantProgramId, String name) {
        return trainingCategoryEntityMapper.convert(trainingCategoryRepository.findByGrantProgramAndName(grantProgramId, name));
    }
}
