package pl.sodexo.it.gryf.service.utils;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTrainingDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;

import java.math.BigDecimal;

/**
 * Created by tomasz.bilski.ext on 2015-09-15.
 */
@ToString
public class ReimbursementCalculationHelper {

    //PUBLIC METHODS - LINIE SZKOLEN

    /**
     * Wylicza wartość godziny szkolenia rozliczana bonami
     * @param t linia szkolania
     * @return wyliczona wartość
     */
    public static BigDecimal calculateVoucherRefundedTrainingHourValue(ReimbursementTraining t){
        BigDecimal wprod = GryfUtils.getValue(t.getProductTotalValue());//Wartość produktu (bonu)
        BigDecimal cbgs = GryfUtils.getValue(t.getTrainingHourGrossPrice()); //Cena brutto godziny szkolenia
        return wprod.min(cbgs);//min (wprod, cbgs)
    }

    /**
     * Wylicza kwotę należną IS od Sodexo
     * @param t linia szkolenia
     * @param voucherRefundedTrainingHourValue wartość godziny szkolenia rozliczana bonami
     *                                         (wartość wyliczana przez calculateVoucherRefundedTrainingHourValue)
     * @return wyliczona wartość
     */
    public static BigDecimal calculateSxoTiAmountDue(ReimbursementTraining t, BigDecimal voucherRefundedTrainingHourValue){
        BigDecimal lp = GryfUtils.getValue(t.getProductsNumber()); //liczba produktów
        BigDecimal wgsrb = GryfUtils.getValue(voucherRefundedTrainingHourValue);//Wartość godziny szkolenia rozliczana bonami
        return wgsrb.multiply(lp);//wgsrb * lp
    }

    /**
     * Wylicza kwotę nalezną IS od MSP
     * @param t linia szkolenia
     * @param sxoTiAmountDue kwotę należną IS od Sodexo
     *                       (wartość wyliczana przez calculateSxoTiAmountDue)
     * @return wyliczona wartość
     */
    public static BigDecimal calculateEntToTiAmountDue(ReimbursementTraining t, BigDecimal sxoTiAmountDue){
        BigDecimal cbgs = GryfUtils.getValue(t.getTrainingHourGrossPrice()); //Cena brutto godziny szkolenia
        BigDecimal ig = GryfUtils.getValue(t.getTrainingHoursTotal());//ilość godzin (szkolenia)
        BigDecimal kniods = GryfUtils.getValue(sxoTiAmountDue);//Kwota należna MŚP od Sodexo
        return cbgs.multiply(ig).subtract(kniods);//KNIodM = CBGS * IG – KNIodS
    }

    /**
     * Wylicza kwotę należna MSP od Sodexo
     * @param t linia szkolenia
     * @return wyliczona wartość
     */
    public static BigDecimal calculateSxoEntAmountDue(ReimbursementTraining t){
        BigDecimal wprod = GryfUtils.getValue(t.getProductTotalValue());//Wartość produktu (bonu)
        BigDecimal cbgs = GryfUtils.getValue(t.getTrainingHourGrossPrice()); //Cena brutto godziny szkolenia
        BigDecimal lp = GryfUtils.getValue(t.getProductsNumber()); //liczba produktów
        return BigDecimal.ZERO.max((wprod.subtract(cbgs)).multiply(lp));//MAX(0, (WPROD – CBGS) * LP)
    }

    /**
     * Wylicza wykorzystany wkład własny MSP
     * @param t linia szkolenia
     * @return wyliczona wartość
     */
    public static BigDecimal calculateUsedOwnEntContributionAmount(ReimbursementTraining t){
        BigDecimal cbgs = GryfUtils.getValue(t.getTrainingHourGrossPrice()); //Cena brutto godziny szkolenia
        BigDecimal wprod = GryfUtils.getValue(t.getProductTotalValue());//Wartość produktu (bonu)
        BigDecimal wpom = GryfUtils.getValue(t.getProductAidValue());//Wartość pomocy (w bonie)
        BigDecimal lp = GryfUtils.getValue(t.getProductsNumber()); //liczba produktów
        return (cbgs.compareTo(wpom) <= 0) ? BigDecimal.ZERO :
                (cbgs.min(wprod).subtract(wpom)).multiply(lp);// cbgs <= wpom   ? 0 : (min(cbgs,wprod) – wpom) * lp
    }

    /**
     * Wylicza wartość dofinanoswania MSP
     * @param t linia szkolenia
     * @return wyliczona wartość
     */
    public static BigDecimal calculateGrantAmount(ReimbursementTraining t){
        BigDecimal wprod = GryfUtils.getValue(t.getProductAidValue()); //Wartość produktu (bonu)
        BigDecimal lp = GryfUtils.getValue(t.getProductsNumber()); //liczba produktów
        return wprod.multiply(lp);//wpom * lp
    }

    /**
     * Wylicza wartość dofinansowania wpłaconego do IS
     * @param sxoTiAmountDue kwotę należną IS od Sodexo
     *                       (wartość wyliczana przez calculateSxoTiAmountDue)
     * @param usedOwnEntContributionAmount wykorzystany wkład własny MSP
     *                                     (wartość wyliczana przez calculateUsedOwnEntContributionAmount)
     * @return wyliczona wartość
     */
    public static BigDecimal calculateGrantAmountPayedToTi(BigDecimal sxoTiAmountDue, BigDecimal usedOwnEntContributionAmount){
        BigDecimal kniods = GryfUtils.getValue(sxoTiAmountDue);
        BigDecimal wwwm = GryfUtils.getValue(usedOwnEntContributionAmount);
        return kniods.subtract(wwwm);//kniods – wwwm
    }

    /**
     * Wylicza całkowity koszt szkolenia
     * @param t linia szkolenia
     * @return wyliczona wartość
     */
    public static BigDecimal calculateTrainingCost(ReimbursementTraining t){
        BigDecimal cbgs = GryfUtils.getValue(t.getTrainingHourGrossPrice());//Cena brutto godziny szkolenia
        BigDecimal ig = GryfUtils.getValue(t.getTrainingHoursTotal());//ilość godzin (szkolenia)
        return cbgs.multiply(ig);//CBGS * IG
    }

    //PUBLIC METHODS - SUMY LINII SZKOLEN

    /**
     * Wylicza kwotę nalezną IS do MSP. Sumuje pola ze szkoleń.
     * @param r rozliczenie
     * @return wyliczona wartość
     */
    public static BigDecimal calculateEntToTiAmountDueTotal(ReimbursementDTO r){
        BigDecimal sum = BigDecimal.ZERO;
        if(r.getReimbursementTrainings() != null){
            for (ReimbursementTrainingDTO trainingDTO : r.getReimbursementTrainings()) {
                sum = sum.add(trainingDTO.getEntToTiAmountDue());
            }
        }
        return sum;
    }

    /**
     * Wylicza wykorzystany wkład własny MSP. Sumuje pola ze szkoleń.
     * @param r rozliczenie
     * @return wyliczona wartość
     */
    public static BigDecimal calculateUsedOwnEntContributionAmountTotal(ReimbursementDTO r){
        BigDecimal sum = BigDecimal.ZERO;
        if(r.getReimbursementTrainings() != null){
            for (ReimbursementTrainingDTO trainingDTO : r.getReimbursementTrainings()) {
                sum = sum.add(trainingDTO.getUsedOwnEntContributionAmount());
            }
        }
        return sum;
    }

    /**
     * Wylicza wartość dofinansowania MSP. Sumuje pola ze szkoleń.
     * @param r rozliczenie
     * @return wyliczona wartość
     */
    public static BigDecimal calculateGrantAmountTotal(ReimbursementDTO r){
        BigDecimal sum = BigDecimal.ZERO;
        if(r.getReimbursementTrainings() != null){
            for (ReimbursementTrainingDTO trainingDTO : r.getReimbursementTrainings()) {
                sum = sum.add(trainingDTO.getGrantAmount());
            }
        }
        return sum;
    }

    /**
     * Wylicza wartosć dofinansowania wpłąconego do IS. Sumuje pola ze szkoleń.
     * @param r rozliczenie
     * @return wyliczona wartość
     */
    public static BigDecimal calculateGrantAmountPayedToTiTotal(ReimbursementDTO r){
        BigDecimal sum = BigDecimal.ZERO;
        if(r.getReimbursementTrainings() != null){
            for (ReimbursementTrainingDTO trainingDTO : r.getReimbursementTrainings()) {
                sum = sum.add(trainingDTO.getGrantAmountPayedToTi());
            }
        }
        return sum;
    }

    /**
     * Wylicza ałkowity koszt szkoleń. Sumuje pola ze szkoleń.
     * @param r rozliczenie
     * @return wyliczona wartość
     */
    public static BigDecimal calculateTrainingCostTotal(ReimbursementDTO r){
        BigDecimal sum = BigDecimal.ZERO;
        if(r.getReimbursementTrainings() != null){
            for (ReimbursementTrainingDTO trainingDTO : r.getReimbursementTrainings()) {
                sum = sum.add(trainingDTO.getTrainingCost());
            }
        }
        return sum;
    }

    /**
     * Sumuje całkowity koszt treningów: Suma (Ilość godzin szkolenia wszystkich uczestników *
     * Cena brutto godziny szkolenia) dla wszystkich szkoleń. Nie liczy na podstawie danych wylicoznych wczesniej lecz na podstyawie prowadzonych danych w dto.
     * Metoda wykorzystywana przy sprawdzeniu czy  Suma (Ilość godzin szkolenia wszystkich uczestników *
     * Cena brutto godziny szkolenia) dla wszystkich szkoleń = kwota brutto faktury
     * @param dto obiekt transferowy
     * @return całkowity koszt treningów
     */
    public static BigDecimal calculateTrainingCostTotal_dependFilledData(ReimbursementDTO dto){
        BigDecimal sum = BigDecimal.ZERO;
        if(dto.getReimbursementTrainings() != null){
            for (ReimbursementTrainingDTO trainingDTO : dto.getReimbursementTrainings()) {
                BigDecimal trainingHourGrossPrice = GryfUtils.getValue(trainingDTO.getTrainingHourGrossPrice());
                BigDecimal trainingHoursTotal = GryfUtils.getValue(trainingDTO.getTrainingHoursTotal());
                sum = sum.add(trainingHourGrossPrice.multiply(trainingHoursTotal));
            }
        }
        return sum;
    }

}
