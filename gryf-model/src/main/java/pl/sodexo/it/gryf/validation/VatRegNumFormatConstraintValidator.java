package pl.sodexo.it.gryf.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VatRegNumFormatConstraintValidator implements ConstraintValidator<VatRegNumFormat, String> {

    @Override
    public void initialize(VatRegNumFormat a) {
    }

    @Override
    public boolean isValid(String nip, ConstraintValidatorContext cvc) {
        if(nip == null){
            return false;
        }

        int size = nip.length();
        if (size != 10) {
            return false;
        }
        int[] weights = {6,5,7,2,3,4,5,6,7};
        int j = 0, sum = 0, control = 0;
        int controlSum = new Integer(nip.substring(size - 1)).intValue();
        for (int i = 0; i < size - 1; i++) {
            char c = nip.charAt(i);
            j = new Integer(String.valueOf(c)).intValue();
            sum += j * weights[i];
        }
        control = sum % 11;
        return (control == controlSum);
    }

}
