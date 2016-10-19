package pl.sodexo.it.gryf.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Klasa narzędziowa do operacji na nr pesel
 *
 * @author jufnal@isolution.pl<br>
 *         created: 19 gru 2014
 *
 */
public final class PeselUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeselUtils.class);

    private PeselUtils() {}

    /**
     * Sprawdza czy numer pesel jest prawidłowy.
     * 
     * @param pesel - numer pesel
     * @return true dla prawidłowego, false dla nieprawidłowego pesela
     */
    public static boolean validate(String pesel) {
        return new PeselUtilsHelper(pesel).validate();
    }

    /**
     * Sprawdza czy osoba o podanym numerze pesel jest mężczyzną.
     *
     * @param pesel - numer pesel
     * @return true dla mężczyzny, false dla kobiety
     */
    public static Boolean isMale(String pesel) {
        PeselUtilsHelper helper = new PeselUtilsHelper(pesel);
        if (!helper.validate()) return null;
        return helper.isMale();
    }

    private static class PeselUtilsHelper {

        private static final int PESEL_LENGTH            = 11;

        private static final int INDEX_YEAR_FIRST_FLAG   = 0;
        private static final int INDEX_YEAR_SECOND_FLAG  = 1;
        private static final int INDEX_MONTH_FIRST_FLAG  = 2;
        private static final int INDEX_MONTH_SECOND_FLAG = 3;
        private static final int INDEX_DAY_FIRST_FLAG    = 4;
        private static final int INDEX_DAY_SECOND_FLAG   = 5;
        
        private static final int INDEX_SEX_FLAG          = 9;
        private static final int INDEX_CHECKSUM_FLAG     = 10;

        private final byte[] peselAsNumberArray;
        private final String pesel;

        PeselUtilsHelper(String pesel) {
            this.pesel = getTrimed(pesel);
            this.peselAsNumberArray = StringUtils.isNumeric(pesel) ? stringToByteNumbers(pesel) :  new byte[0];
        }

        private String getTrimed(String inputPesel) {
            if(inputPesel != null) return inputPesel.trim();
            return "";
        }

        private static byte[] stringToByteNumbers(String value) {
            String[] chars = value.split("");
            byte[] numbers = new byte[PESEL_LENGTH];

            int i = 0;
            for (String c : chars) {
                if(c.length() != 1) continue;
                numbers[i] = Byte.valueOf(c);
                i += 1;

                if(i>=PESEL_LENGTH) break;
            }

            return numbers;
        }
        
        public boolean validate() {
            if ((pesel != null && pesel.length() != PESEL_LENGTH) || peselAsNumberArray.length != PESEL_LENGTH) {
                LOGGER.debug("Podany PESEL nie ma 11 znakow, pesel={}", pesel);
                return false;
            }

            if (!StringUtils.isNumeric(pesel)) {
                LOGGER.debug("Podany PESEL zawiera inne znaki niz cyfry, pesel={}", pesel);
                return false;
            }

            if (calculateChecksum()!= peselAsNumberArray[INDEX_CHECKSUM_FLAG]) {
                LOGGER.debug("Podany PESEL ma nieprawidlowa sume kontrolna, pesel={}", pesel);
                return false;
            }
            
            return true;
        }

        private int calculateChecksum() {
            int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
            
            int checksum = 0;
            for (int i = 0; i < PESEL_LENGTH-1; i++) {
                checksum += weights[i] * peselAsNumberArray[i];
            }
            checksum %= 10;
            checksum = 10 - checksum;
            checksum %= 10;
            return checksum;
        }

        public int getYear() {
            int year;
            int month;
            year = 10 *  peselAsNumberArray[INDEX_YEAR_FIRST_FLAG];
            year +=      peselAsNumberArray[INDEX_YEAR_SECOND_FLAG];
            month = 10 * peselAsNumberArray[INDEX_MONTH_FIRST_FLAG];
            month +=     peselAsNumberArray[INDEX_MONTH_SECOND_FLAG];
            
            if (month > 80 && month < 93) {
                year += 1800;
            } else if (month > 0 && month < 13) {
                year += 1900;
            } else if (month > 20 && month < 33) {
                year += 2000;
            } else if (month > 40 && month < 53) {
                year += 2100;
            } else if (month > 60 && month < 73) {
                year += 2200;
            }
            return year;
        }

        public int getMonth() {
            int month;
            month = 10 * peselAsNumberArray[INDEX_MONTH_FIRST_FLAG];
            month +=     peselAsNumberArray[INDEX_MONTH_SECOND_FLAG];
            
            if (month > 80 && month < 93) {
                month -= 80;
            } else if (month > 20 && month < 33) {
                month -= 20;
            } else if (month > 40 && month < 53) {
                month -= 40;
            } else if (month > 60 && month < 73) {
                month -= 60;
            }
            return month;
        }

        public int getDay() {
            int day;
            day = 10 * peselAsNumberArray[INDEX_DAY_FIRST_FLAG];
            day +=     peselAsNumberArray[INDEX_DAY_SECOND_FLAG];
            return day;
        }

        public boolean isMale() {
            return peselAsNumberArray[INDEX_SEX_FLAG] % 2 != 0;
        }
    }
}