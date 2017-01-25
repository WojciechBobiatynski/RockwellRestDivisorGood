package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementSettleAndVerify;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-07.
 */
@ToString
public class ReimbursementTrainingDTO extends GryfDto {

    private Long id;

    @NotEmpty(message = "Nazwa usługi nie może być pusta")
    @Size(message = "Nazwa usługi może mieć maksymalnie 200 znaków", max = 200)
    private String trainingName;

    @NotNull(message = "Data rozpoczęcia usługi nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private Date trainingDateFrom;

    @NotNull(message = "Data zakończenia usługi nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private Date trainingDateTo;

    @NotEmpty(message = "Miejsce usługi nie może być puste", groups = ValidationGroupReimbursementSettleAndVerify.class)
    @Size(message = "Miejsce usługi może mieć maksymalnie 100 znaków", max = 100)
    private String trainingPlace;

    @NotNull(message = "Rodzaj dofinanoswania nie może być puste", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private String grantOwnerAidProductId;

    @NotNull(message = "Liczba produktów nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private BigDecimal productsNumber;

    /**
     * Cena brutto godziny usługi z systemu RUR
     */
    @NotNull(message = "Cena brutto godziny usługi nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private BigDecimal trainingHourGrossPrice;

    /**
     * Sumaryczna ilość godzin usługi dla wszystkich uczestników
     */
    @NotNull(message = "Sumaryczna ilość godz. usługi wszystkich ucz. nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private BigDecimal trainingHoursTotal;

    /**
     * Wartość produktu
     */
    private BigDecimal productTotalValue;

    /**
     * Wartość pomocy
     */
    private BigDecimal productAidValue;

    /**
     * Wartość godziny usługi rozliczona bonami (wyliczna)
     */
    private BigDecimal voucherRefundedTrainingHourValue;

    /**
     * Kwota należna Usługodawcy od Sodexo
     */
    private BigDecimal sxoTiAmountDue;

    /**
     * Kwota nalezna IS od MSP  (wyliczna)
     */
    private BigDecimal entToTiAmountDue;

    /**
     * Kwota należna MSP od Sodexo
     */
    private BigDecimal sxoEntAmountDue;

    /**
     * Wykorzystany wkład własny MSP (wyliczane)
     */
    private BigDecimal usedOwnEntContributionAmount;

    /**
     * Wartość dofinansowania MSP (wyliczane)
     */
    private BigDecimal grantAmount;

    /**
     * Wartosć dofinansowania wpłąconego do IS (wyliczane)
     */
    private BigDecimal grantAmountPayedToTi;

    /**
     * Koszt usług (wyliczane)
     */
    private BigDecimal trainingCost;

    @Valid
    private List<ReimbursementTraineeDTO> reimbursementTrainees;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getTrainingDateFrom() {
        return trainingDateFrom;
    }

    public void setTrainingDateFrom(Date trainingDateFrom) {
        this.trainingDateFrom = trainingDateFrom;
    }

    public Date getTrainingDateTo() {
        return trainingDateTo;
    }

    public void setTrainingDateTo(Date trainingDateTo) {
        this.trainingDateTo = trainingDateTo;
    }

    public String getTrainingPlace() {
        return trainingPlace;
    }

    public void setTrainingPlace(String trainingPlace) {
        this.trainingPlace = trainingPlace;
    }

    public String getGrantOwnerAidProductId() {
        return grantOwnerAidProductId;
    }

    public void setGrantOwnerAidProductId(String grantOwnerAidProductId) {
        this.grantOwnerAidProductId = grantOwnerAidProductId;
    }

    public BigDecimal getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(BigDecimal productsNumber) {
        this.productsNumber = productsNumber;
    }

    public BigDecimal getTrainingHourGrossPrice() {
        return trainingHourGrossPrice;
    }

    public void setTrainingHourGrossPrice(BigDecimal trainingHourGrossPrice) {
        this.trainingHourGrossPrice = trainingHourGrossPrice;
    }

    public BigDecimal getTrainingHoursTotal() {
        return trainingHoursTotal;
    }

    public void setTrainingHoursTotal(BigDecimal trainingHoursTotal) {
        this.trainingHoursTotal = trainingHoursTotal;
    }

    public BigDecimal getProductTotalValue() {
        return productTotalValue;
    }

    public void setProductTotalValue(BigDecimal productTotalValue) {
        this.productTotalValue = productTotalValue;
    }

    public BigDecimal getProductAidValue() {
        return productAidValue;
    }

    public void setProductAidValue(BigDecimal productAidValue) {
        this.productAidValue = productAidValue;
    }

    public BigDecimal getVoucherRefundedTrainingHourValue() {
        return voucherRefundedTrainingHourValue;
    }

    public void setVoucherRefundedTrainingHourValue(BigDecimal voucherRefundedTrainingHourValue) {
        this.voucherRefundedTrainingHourValue = voucherRefundedTrainingHourValue;
    }

    public BigDecimal getSxoTiAmountDue() {
        return sxoTiAmountDue;
    }

    public void setSxoTiAmountDue(BigDecimal sxoTiAmountDue) {
        this.sxoTiAmountDue = sxoTiAmountDue;
    }

    public BigDecimal getEntToTiAmountDue() {
        return entToTiAmountDue;
    }

    public void setEntToTiAmountDue(BigDecimal entToTiAmountDue) {
        this.entToTiAmountDue = entToTiAmountDue;
    }

    public BigDecimal getSxoEntAmountDue() {
        return sxoEntAmountDue;
    }

    public void setSxoEntAmountDue(BigDecimal sxoEntAmountDue) {
        this.sxoEntAmountDue = sxoEntAmountDue;
    }

    public BigDecimal getUsedOwnEntContributionAmount() {
        return usedOwnEntContributionAmount;
    }

    public void setUsedOwnEntContributionAmount(BigDecimal usedOwnEntContributionAmount) {
        this.usedOwnEntContributionAmount = usedOwnEntContributionAmount;
    }

    public BigDecimal getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(BigDecimal grantAmount) {
        this.grantAmount = grantAmount;
    }

    public BigDecimal getGrantAmountPayedToTi() {
        return grantAmountPayedToTi;
    }

    public void setGrantAmountPayedToTi(BigDecimal grantAmountPayedToTi) {
        this.grantAmountPayedToTi = grantAmountPayedToTi;
    }

    public BigDecimal getTrainingCost() {
        return trainingCost;
    }

    public void setTrainingCost(BigDecimal trainingCost) {
        this.trainingCost = trainingCost;
    }

    public List<ReimbursementTraineeDTO> getReimbursementTrainees() {
        return reimbursementTrainees;
    }

    public void setReimbursementTrainees(List<ReimbursementTraineeDTO> reimbursementTrainees) {
        this.reimbursementTrainees = reimbursementTrainees;
    }
}
