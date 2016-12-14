package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportContractDTO {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator umowy nie może być pusty")
    private Long id;

    @Getter
    @Setter
    @NotNull(message = "Program dofinansowania nie może być pusty")
    private Long grantProgramId;

    @Getter
    @Setter
    @NotEmpty(message = "Rodzaj umowy nie może być pusty")
    private String contractType;

    @Getter
    @Setter
    @NotNull(message = "Data podpisania umowy nie może być pusta")
    private Date signDate;

    @Getter
    @Setter
    @NotNull(message = "Data ważności umowy nie może być pusta")
    private Date expiryDate;

    @Getter
    @Setter
    @NotEmpty(message = "Kategorie szkolenia przydzielone uczestnikowi nie mogą być puste")
    private String contractTrainingCategories;

    private List<String> contractTrainingCategoryList;

    //EXTRA GETETRS

    public List<String> getContractTrainingCategoryList(){
        if(contractTrainingCategoryList == null) {
            contractTrainingCategoryList = Strings.isNullOrEmpty(contractTrainingCategories) ? Lists.newArrayList() :
                                            Lists.newArrayList(contractTrainingCategories.split(","));
        }
        return contractTrainingCategoryList;
    }
}
