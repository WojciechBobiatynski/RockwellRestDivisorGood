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

    public void calculateChargers(CalculationChargesParamsDto params) {
        calculateSxoTiAmount(params);
        calculateIndTiAmount(params);
        calculateSxoIndAmount(params);
        calculateIndOwnContributionUsed(params);
        calculateIndSubsidyValue(params);
    }

    private void calculateSxoTiAmount(CalculationChargesParamsDto params) {
        if (reimburseIsTraining(params)) {
            calculateSxoTiAmountForTrainings(params);
        } else {
            calculateSxoTiAmountForExam(params);
        }

    }

    private void calculateIndTiAmount(CalculationChargesParamsDto params) {
        if (reimburseIsTraining(params)) {
            calculateIndTiAmountForTraining(params);
        } else {
            calculateIndTiAmountForExam(params);
        }
    }

    private void calculateSxoIndAmount(CalculationChargesParamsDto params) {
        if (reimburseIsTraining(params)) {
            calculateSxoIndAmountForTraining(params);
        } else {
            calculateSxoIndAmountForExam(params);
        }
    }

    private void calculateSxoIndAmountForTraining(CalculationChargesParamsDto params) {
        if (isTrainingHourPriceLowerThanProductValue(params)) {
            setSxoIndAmountDueTotal(params.getProductValue().multiply(new BigDecimal(params.getUsedProductsNumber()))
                    .subtract(new BigDecimal(params.getUsedProductsNumber() / params.getProductInstanceForHour()).multiply(params.getTrainingHourPrice())));
        } else {
            setSxoIndAmountDueTotal(BigDecimal.ZERO);
        }
    }

    private void calculateSxoIndAmountForExam(CalculationChargesParamsDto params) {
        BigDecimal trainingProductCost = params.getTrainingPrice().divide(new BigDecimal(params.getUsedProductsNumber()));
        Integer totalTrainingProductCost = trainingProductCost.setScale(0, RoundingMode.UP).intValue();
        if (!isTrainingProductCostInteger(trainingProductCost) && params.getUsedProductsNumber().equals(totalTrainingProductCost)) {
            //TODO wyciągnąć procentowy wkład własny ze zmiennych
            BigDecimal ownContributionPercentage = new BigDecimal("0.13");
            setSxoIndAmountDueTotal((params.getProductValue().multiply(new BigDecimal(totalTrainingProductCost))).subtract(params.getTrainingPrice()).multiply(ownContributionPercentage)
                    .setScale(2, RoundingMode.HALF_UP));
        }
        setSxoIndAmountDueTotal(BigDecimal.ZERO);
    }

    private boolean isTrainingProductCostInteger(BigDecimal trainingProductCost) {
        return trainingProductCost.scale() <= 0;
    }

    private boolean isTrainingHourPriceLowerThanProductValue(CalculationChargesParamsDto params) {
        return params.getTrainingHourPrice().compareTo(params.getProductValue().multiply(new BigDecimal(params.getProductInstanceForHour()))) < 0;
    }

    private void calculateIndOwnContributionUsed(CalculationChargesParamsDto params) {
        //TODO wyciągnąć procentowy wkład własny ze zmiennych
        BigDecimal ownContributionPercentage = new BigDecimal("0.13");
        setIndOwnContributionUsed(ownContributionPercentage.multiply(getSxoTiAmountDueTotal()).setScale(2, RoundingMode.HALF_UP));
    }

    private void calculateIndSubsidyValue(CalculationChargesParamsDto params) {
        //TODO wyciągnąć procentową wartość dofinansowania
        BigDecimal indSubsidyPercentage = new BigDecimal("0.87");
        setIndSubsidyValue(indSubsidyPercentage.multiply(getSxoTiAmountDueTotal()).setScale(2, RoundingMode.HALF_UP));
    }

    private boolean reimburseIsTraining(CalculationChargesParamsDto params) {
        return params.getMaxProductInstance() == null;
    }

    private void calculateSxoTiAmountForTrainings(CalculationChargesParamsDto params) {
        BigDecimal normalizeHourPrice = new BigDecimal(params.getProductInstanceForHour()).multiply(params.getProductValue());
        BigDecimal realHourPrice = params.getTrainingHourPrice().compareTo(normalizeHourPrice) < 0 ? params.getTrainingHourPrice() : normalizeHourPrice;
        BigDecimal sxoAmount = new BigDecimal(params.getUsedProductsNumber()).multiply(realHourPrice).divide(new BigDecimal(params.getProductInstanceForHour()));
        setSxoTiAmountDueTotal(sxoAmount);
    }

    private void calculateSxoTiAmountForExam(CalculationChargesParamsDto params) {
        BigDecimal usedProductsAmount = new BigDecimal(params.getUsedProductsNumber()).multiply(params.getProductValue());
        if (usedProductsAmount.compareTo(params.getTrainingPrice()) > 0) {
            setSxoTiAmountDueTotal(params.getTrainingPrice());
        } else {
            setSxoTiAmountDueTotal(usedProductsAmount);
        }

    }

    private void calculateIndTiAmountForTraining(CalculationChargesParamsDto params) {
        BigDecimal normalizedProductHourPrice = new BigDecimal(params.getProductInstanceForHour()).multiply(params.getProductValue());
        BigDecimal trainingHourDifferenceCost = BigDecimal.ZERO;
        Integer hoursPaidWithCash = params.getTrainingHoursNumber() - params.getUsedProductsNumber() / params.getProductInstanceForHour();
        if (params.getTrainingHourPrice().compareTo(normalizedProductHourPrice) > 0) {
            trainingHourDifferenceCost = new BigDecimal(params.getUsedProductsNumber() / params.getProductInstanceForHour())
                    .multiply(params.getTrainingHourPrice().subtract(normalizedProductHourPrice));
        }
        BigDecimal hoursPaidWithCashCost = new BigDecimal(hoursPaidWithCash).multiply(params.getTrainingHourPrice());
        setIndTiAmountDueTotal(trainingHourDifferenceCost.add(hoursPaidWithCashCost));
    }

    private void calculateIndTiAmountForExam(CalculationChargesParamsDto params) {
        setIndTiAmountDueTotal(params.getTrainingPrice().subtract(getSxoTiAmountDueTotal()));
    }
}
