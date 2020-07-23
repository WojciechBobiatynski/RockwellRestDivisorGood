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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.BIG_DECIMAL_MONEY_SCALE;
import static pl.sodexo.it.gryf.common.utils.GryfConstants.BIG_DECIMAL_SUM_SCALE;

/**
 * Podstawowe dto dla listy rozliczeń elektronicznych
 * <p>
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
    private Date arrivalDate;

    @Getter
    @Setter
    private CorrectionDto lastCorrectionDto;

    @Getter
    @Setter
    private List<ErmbsReportDto> reports;

    @Getter
    @Setter
    private List<ErmbsMailDto> emails;

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

    @Getter
    @Setter
    private List<ElctRmbsLineDto> elctRmbsLineDtos;

    public void calculateChargers(CalculationChargesParamsDto params) {
        calculateSxoTiAmount(params);
        calculateIndTiAmount(params);
        calculateSxoIndAmount(params);
        calculateIndOwnContributionUsed(params);
        calculateIndSubsidyValue(params);
    }

    public void completeElctRmbsLines(List<CalculationChargesOrderParamsDto> orderParams, List<Long> elctRmbsLineIds) {
        Iterator<Long> iterator = elctRmbsLineIds.iterator();

        List<ElctRmbsLineDto> elctRmbsLineDtos = orderParams.stream()
                .map(orderParamsDto -> {
                    ElctRmbsLineDto elctRmbsLineDto = new ElctRmbsLineDto();
                    elctRmbsLineDto.setId(iterator.hasNext()?iterator.next():null);
                    elctRmbsLineDto.setOrderId(orderParamsDto.getOrderId());
                    elctRmbsLineDto.setUsedProductsNumber(orderParamsDto.getOrderUsedProductsNumber());
                    elctRmbsLineDto.setOwnContributionPercentage(orderParamsDto.getOwnContributionPercentage());
                    elctRmbsLineDto.setIndSubsidyPercentage(orderParamsDto.getIndSubsidyPercentage());
                    return elctRmbsLineDto;
                })
                .collect(Collectors.toList());

        setElctRmbsLineDtos(elctRmbsLineDtos);
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

    private void calculateIndOwnContributionUsed(CalculationChargesParamsDto params) {
        BigDecimal indOwnAmountSum = getElctRmbsLineDtos().stream()
                .map(elctRmbsLineDto -> {
                    BigDecimal indOwnAmount = elctRmbsLineDto.getOwnContributionPercentage()
                            .multiply(elctRmbsLineDto.getSxoTiAmountDueTotal())
                            .setScale(BIG_DECIMAL_SUM_SCALE, RoundingMode.HALF_UP);
                    elctRmbsLineDto.setIndOwnContributionUsed(indOwnAmount);
                    return indOwnAmount;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        setIndOwnContributionUsed(indOwnAmountSum.setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }

    private void calculateIndSubsidyValue(CalculationChargesParamsDto params) {
        BigDecimal indSubsidyAmountSum = getElctRmbsLineDtos().stream()
                .map(elctRmbsLineDto -> {
                    BigDecimal indSubsidyAmount = elctRmbsLineDto.getIndSubsidyPercentage()
                            .multiply(elctRmbsLineDto.getSxoTiAmountDueTotal())
                            .setScale(BIG_DECIMAL_SUM_SCALE, RoundingMode.HALF_UP);
                    elctRmbsLineDto.setIndSubsidyValue(indSubsidyAmount);
                    return indSubsidyAmount;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        setIndSubsidyValue(indSubsidyAmountSum.setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }

    private boolean isReimbursementForTraining(CalculationChargesParamsDto params) {
        return params.getMaxProductInstance() == null;
    }

    // calculateSxoTiAmount

    private void calculateSxoTiAmountForTrainings(CalculationChargesParamsDto params) {
        BigDecimal normalizeHourPrice = BigDecimal.valueOf(params.getProductInstanceForHour()).multiply(params.getProductValue());
        BigDecimal realHourPrice = params.getTrainingHourPrice().compareTo(normalizeHourPrice) < 0 ? params.getTrainingHourPrice() : normalizeHourPrice;
        BigDecimal productInstanceForHour = BigDecimal.valueOf(params.getProductInstanceForHour());

        BigDecimal sxoAmountSum = getElctRmbsLineDtos().stream()
                .map(elctRmbsLineDto -> {
                    BigDecimal sxoAmount = BigDecimal.valueOf(elctRmbsLineDto.getUsedProductsNumber())
                            .multiply(realHourPrice)
                            .divide(productInstanceForHour)
                            .setScale(BIG_DECIMAL_SUM_SCALE, RoundingMode.HALF_UP);
                    elctRmbsLineDto.setSxoTiAmountDueTotal(sxoAmount);
                    return sxoAmount;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        setSxoTiAmountDueTotal(sxoAmountSum.setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }

    private void calculateSxoTiAmountForExam(CalculationChargesParamsDto params) {
        BigDecimal usedProductsAmount = BigDecimal.valueOf(params.getUsedProductsNumber()).multiply(params.getProductValue());

        BigDecimal usedProductsAmountSum = getElctRmbsLineDtos().stream()
                .map(elctRmbsLineDto -> {
                    BigDecimal sxoAmount = getSxoTiAmountForExamByUsedOrderProduct(params, usedProductsAmount, elctRmbsLineDto)
                            .setScale(BIG_DECIMAL_SUM_SCALE, RoundingMode.HALF_UP);
                    elctRmbsLineDto.setSxoTiAmountDueTotal(sxoAmount);
                    return sxoAmount;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        setSxoTiAmountDueTotal(usedProductsAmountSum.setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }

    private BigDecimal getSxoTiAmountForExamByUsedOrderProduct(CalculationChargesParamsDto params, BigDecimal usedProductsAmount, ElctRmbsLineDto elctRmbsLineDto) {
        if (usedProductsAmount.compareTo(params.getTrainingPrice()) > 0) {
            return params.getTrainingPrice()
                    .multiply(BigDecimal.valueOf(elctRmbsLineDto.getUsedProductsNumber()))
                    .divide(BigDecimal.valueOf(params.getUsedProductsNumber()));
        } else {
            return BigDecimal.valueOf(elctRmbsLineDto.getUsedProductsNumber())
                    .multiply(params.getProductValue());
        }
    }

    // calculateIndTiAmount

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

    // calculateSxoIndAmount

    private void calculateSxoIndAmountForTraining(CalculationChargesParamsDto params) {
        BigDecimal sxoIndAmountSum = BigDecimal.ZERO;

        if (isTrainingHourPriceLowerThanProductValue(params)) {
            sxoIndAmountSum = getElctRmbsLineDtos().stream()
                    .map(elctRmbsLineDto -> {
                        BigDecimal sxoAmount = params.getProductValue()
                                .multiply(BigDecimal.valueOf(elctRmbsLineDto.getUsedProductsNumber()))
                                .subtract(BigDecimal.valueOf(elctRmbsLineDto.getUsedProductsNumber()/params.getProductInstanceForHour())
                                        .multiply(params.getTrainingHourPrice()))
                                .multiply(elctRmbsLineDto.getOwnContributionPercentage())
                                .setScale(BIG_DECIMAL_SUM_SCALE, RoundingMode.HALF_UP);
                        elctRmbsLineDto.setSxoIndAmountDueTotal(sxoAmount);
                        return sxoAmount;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        setSxoIndAmountDueTotal(sxoIndAmountSum.setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }

    private boolean isTrainingHourPriceLowerThanProductValue(CalculationChargesParamsDto params) {
        return params.getTrainingHourPrice().compareTo(params.getProductValue().multiply(BigDecimal.valueOf(params.getProductInstanceForHour()))) < 0;
    }

    private void calculateSxoIndAmountForExam(CalculationChargesParamsDto params) {
        if (isExamCheaperThanLimit(params)) {
            calculateSxoIndAmountForTrainingIfCheaperThanLimit(params);
        } else {
            setSxoIndAmountDueTotal(BigDecimal.ZERO);
        }
    }

    private boolean isExamCheaperThanLimit(CalculationChargesParamsDto params) {
        return params.getTrainingPrice().compareTo(BigDecimal.valueOf(params.getMaxProductInstance()).multiply(params.getProductValue())) < 0;
    }

    private void calculateSxoIndAmountForTrainingIfCheaperThanLimit(CalculationChargesParamsDto params) {
        BigDecimal usedProductForExamCost = BigDecimal.valueOf(params.getUsedProductsNumber()).multiply(params.getProductValue());
        BigDecimal sxoIndAmountSum = BigDecimal.ZERO;

        if (usedProductForExamCost.compareTo(params.getTrainingPrice()) > 0) {
            sxoIndAmountSum = getElctRmbsLineDtos().stream()
                    .map(elctRmbsLineDto -> {
                        BigDecimal sxoAmount = BigDecimal.valueOf(elctRmbsLineDto.getUsedProductsNumber())
                                .multiply(params.getProductValue())
                                .subtract(params.getTrainingPrice()
                                        .multiply(BigDecimal.valueOf(elctRmbsLineDto.getUsedProductsNumber()))
                                        .divide(BigDecimal.valueOf(params.getUsedProductsNumber())))
                                .multiply(elctRmbsLineDto.getOwnContributionPercentage())
                                .setScale(BIG_DECIMAL_SUM_SCALE, RoundingMode.HALF_UP);
                        elctRmbsLineDto.setSxoIndAmountDueTotal(sxoAmount);
                        return sxoAmount;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        setSxoIndAmountDueTotal(sxoIndAmountSum.setScale(BIG_DECIMAL_MONEY_SCALE, RoundingMode.HALF_UP));
    }
}
