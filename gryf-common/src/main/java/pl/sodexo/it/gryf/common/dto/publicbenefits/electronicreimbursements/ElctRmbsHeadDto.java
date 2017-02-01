package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.ProductHeadDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.BIG_DECIMAL_MONEY_SCALE;

/**
 * Podstawowe dto dla listy rozliczeń elektronicznych
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class ElctRmbsHeadDto extends VersionableDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private String typeCode;

    @Getter
    @Setter
    private Long trainingInstanceId;

    @Getter
    @Setter
    private Long trainingInstitutionId;

    @Getter
    @Setter
    private Long poolId;

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private List<ProductHeadDto> products;

    @Getter
    @Setter
    private BigDecimal sxoTiAmountDueTotal;

    @Getter
    @Setter
    private BigDecimal sxoIndAmountDueTotal;

    @Getter
    @Setter
    private BigDecimal indTiAmountDueTotal;

    @Getter
    @Setter
    private BigDecimal indOwnContributionUsed;

    @Getter
    @Setter
    private BigDecimal indSubsidyValue;

    @Getter
    @Setter
    private List<ErmbsAttachmentDto> attachments;

    @Getter
    @Setter
    private String statusCode;

    @Getter
    @Setter
    private String statusName;

    @Getter
    @Setter
    private String tiReimbAccountNumber;

    @Getter
    @Setter
    private Date requiredCorrectionDate;

    @Getter
    @Setter
    private Date reimbursementDate;

    @Getter
    @Setter
    private CorrectionDto lastCorrectionDto;

    @Getter
    @Setter
    private List <ErmbsReportDto> reports;

    @Getter
    @Setter
    private List <ErmbsMailDto> emails;

    @Getter
    @Setter
    private boolean opinionDone;

    @Getter
    @Setter
    private Integer expiredProductsNum;

    @Getter
    @Setter
    private boolean terminated;

    @Getter
    @Setter
    private Long rejectionReasonId;

    @Getter
    @Setter
    private String rejectionDetails;

    @Getter
    @Setter
    private boolean automatic;

    public void calculateChargers(CalculationChargesParamsDto params) {
        calculateSxoTiAmount(params);
        calculateIndTiAmount(params);
        calculateSxoIndAmount(params);
        calculateIndOwnContributionUsed(params);
        calculateIndSubsidyValue(params);
    }

    private void calculateSxoTiAmount(CalculationChargesParamsDto params) {
        if (isReimbursementForTraining(params)) {
            calculateSxoTiAmountForTrainings(params);
        } else {
            calculateSxoTiAmountForExam(params);
        }

    }

    private void calculateIndTiAmount(CalculationChargesParamsDto params) {
        if (isReimbursementForTraining(params)) {
            calculateIndTiAmountForTraining(params);
        } else {
            calculateIndTiAmountForExam(params);
        }
    }

    private void calculateSxoIndAmount(CalculationChargesParamsDto params) {
        if (isReimbursementForTraining(params)) {
            calculateSxoIndAmountForTraining(params);
        } else {
            calculateSxoIndAmountForExam(params);
        }
    }

    private void calculateSxoIndAmountForTraining(CalculationChargesParamsDto params) {
        if (isTrainingHourPriceLowerThanProductValue(params)) {
            BigDecimal tempValue = params.getProductValue().multiply(BigDecimal.valueOf(params.getUsedProductsNumber()))
                    .subtract(BigDecimal.valueOf(params.getUsedProductsNumber() / params.getProductInstanceForHour()).multiply(params.getTrainingHourPrice()));
            setSxoIndAmountDueTotal(tempValue.multiply(params.getOwnContributionPercentage()));
        } else {
            setSxoIndAmountDueTotal(BigDecimal.ZERO);
        }
    }

    private void calculateSxoIndAmountForExam(CalculationChargesParamsDto params) {
        if(isExamCheaperThanLimit(params)) {
            calculateSxoIndAmountForTrainingIfCheaperThanLimit(params);
        } else {
            setSxoIndAmountDueTotal(BigDecimal.ZERO);
        }
    }

    private void calculateSxoIndAmountForTrainingIfCheaperThanLimit(CalculationChargesParamsDto params) {
        BigDecimal usedProductForExamCost = BigDecimal.valueOf(params.getUsedProductsNumber()).multiply(params.getProductValue());
        if(usedProductForExamCost.compareTo(params.getTrainingPrice()) > 0){
            setSxoIndAmountDueTotal(usedProductForExamCost.subtract(params.getTrainingPrice()).multiply(params.getOwnContributionPercentage()).setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
        } else {
            setSxoIndAmountDueTotal(BigDecimal.ZERO);
        }
    }

    private boolean isExamCheaperThanLimit(CalculationChargesParamsDto params) {
        return params.getTrainingPrice().compareTo(BigDecimal.valueOf(params.getMaxProductInstance()).multiply(params.getProductValue())) < 0;
    }

    private boolean isTrainingHourPriceLowerThanProductValue(CalculationChargesParamsDto params) {
        return params.getTrainingHourPrice().compareTo(params.getProductValue().multiply(BigDecimal.valueOf(params.getProductInstanceForHour()))) < 0;
    }

    private void calculateIndOwnContributionUsed(CalculationChargesParamsDto params) {
        setIndOwnContributionUsed(params.getOwnContributionPercentage().multiply(getSxoTiAmountDueTotal()).setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }

    private void calculateIndSubsidyValue(CalculationChargesParamsDto params) {
        setIndSubsidyValue(params.getIndSubsidyPercentage().multiply(getSxoTiAmountDueTotal()).setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }

    private boolean isReimbursementForTraining(CalculationChargesParamsDto params) {
        return params.getMaxProductInstance() == null;
    }

    private void calculateSxoTiAmountForTrainings(CalculationChargesParamsDto params) {
        BigDecimal normalizeHourPrice = BigDecimal.valueOf(params.getProductInstanceForHour()).multiply(params.getProductValue());
        BigDecimal realHourPrice = params.getTrainingHourPrice().compareTo(normalizeHourPrice) < 0 ? params.getTrainingHourPrice() : normalizeHourPrice;
        BigDecimal sxoAmount = BigDecimal.valueOf(params.getUsedProductsNumber()).multiply(realHourPrice).divide(BigDecimal.valueOf(params.getProductInstanceForHour()));
        setSxoTiAmountDueTotal(sxoAmount);
    }

    private void calculateSxoTiAmountForExam(CalculationChargesParamsDto params) {
        BigDecimal usedProductsAmount = BigDecimal.valueOf(params.getUsedProductsNumber()).multiply(params.getProductValue());
        if (usedProductsAmount.compareTo(params.getTrainingPrice()) > 0) {
            setSxoTiAmountDueTotal(params.getTrainingPrice());
        } else {
            setSxoTiAmountDueTotal(usedProductsAmount);
        }

    }

    private void calculateIndTiAmountForTraining(CalculationChargesParamsDto params) {
        BigDecimal normalizedProductHourPrice = BigDecimal.valueOf(params.getProductInstanceForHour()).multiply(params.getProductValue());
        BigDecimal trainingHourDifferenceCost = BigDecimal.ZERO;
        Integer hoursPaidWithCash = params.getTrainingHoursNumber() - params.getUsedProductsNumber() / params.getProductInstanceForHour();
        if (params.getTrainingHourPrice().compareTo(normalizedProductHourPrice) > 0) {
            trainingHourDifferenceCost = BigDecimal.valueOf(params.getUsedProductsNumber() / params.getProductInstanceForHour())
                    .multiply(params.getTrainingHourPrice().subtract(normalizedProductHourPrice));
        }
        BigDecimal hoursPaidWithCashCost = BigDecimal.valueOf(hoursPaidWithCash).multiply(params.getTrainingHourPrice());
        setIndTiAmountDueTotal(trainingHourDifferenceCost.add(hoursPaidWithCashCost));
    }

    private void calculateIndTiAmountForExam(CalculationChargesParamsDto params) {
        setIndTiAmountDueTotal(params.getTrainingPrice().subtract(getSxoTiAmountDueTotal()));
    }
}
