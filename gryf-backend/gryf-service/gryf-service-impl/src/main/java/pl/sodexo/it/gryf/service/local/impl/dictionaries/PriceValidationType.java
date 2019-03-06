package pl.sodexo.it.gryf.service.local.impl.dictionaries;

/**
 * Sposób walidacji ceny usługi z iloscią godzin oraz ceną za 1h usługi
 *
 * Created by Damian.PTASZYNSKI on 2019-03-06.
 */
public enum PriceValidationType {

    /**
     * Zaokrąglanie 1 godziny usługi do 2 miejsc po przecinku
     */
    PRICE_ROUNDED("ZAOKR");

    private String code;

    PriceValidationType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
