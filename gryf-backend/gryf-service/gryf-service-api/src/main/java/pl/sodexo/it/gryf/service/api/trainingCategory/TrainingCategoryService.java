package pl.sodexo.it.gryf.service.api.trainingCategory;

import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingcategory.TrainingCategoryDTO;

public interface TrainingCategoryService {

    /**
     * Wyszukiwanie kategorii dla danego programu.
     * Dane powinny byc cache'owane
     *
     * @param grantProgramId
     * @param name
     * @return Kategoria szkolenia dla danego programu
     */
    TrainingCategoryDTO findByGrantProgramAndName(Long grantProgramId, String name);
}
