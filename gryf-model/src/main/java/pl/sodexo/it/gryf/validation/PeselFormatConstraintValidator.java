package pl.sodexo.it.gryf.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeselFormatConstraintValidator implements ConstraintValidator<PeselFormat, String> {

    @Override
    public void initialize(PeselFormat a) {
    }

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext cvc) {
        if (pesel == null){
            return false;
        }

        int size = pesel.length();
        if (size != 11) {
            return false;
        }
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;
        int controlSum = Integer.parseInt(pesel.substring(size - 1));
        for (int i = 0; i < size-1; i++) {
            char c = pesel.charAt(i);
            sum += Integer.parseInt(String.valueOf(c)) * weights[i];
        }
        
        sum %= 10;
        sum = 10-sum;
        sum %= 10;
        
        return (sum == controlSum);
    }

}
