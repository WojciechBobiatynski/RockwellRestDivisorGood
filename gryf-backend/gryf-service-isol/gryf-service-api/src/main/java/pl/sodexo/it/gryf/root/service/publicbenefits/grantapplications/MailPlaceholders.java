package pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jbentyn on 2016-09-20.
 */
//TODO przenieść do commons albo wyekstraktować metody korzystające z tego typu do interfejsów lokalnych
public class MailPlaceholders {

    //FIELDS

    private static final String PLACEHOLDER_PREFIX = "{$";
    private static final String PLACEHOLDER_SUFFIX = "}";
    private Map<String, String> mailPlaceholders;

    //CONSTRUCTORS

    public MailPlaceholders(String name, String value) {
        this.mailPlaceholders = new HashMap();
        add(name, value);
    }

    //PUBLIC METHODS

    /**
     * Dodaje placeholder do struktury
     *
     * @param name nazwa placeholdera
     * @param value wartość placeholdera
     * @return wartosc placeholdera
     */
    public MailPlaceholders add(String name, String value) {
        mailPlaceholders.put(PLACEHOLDER_PREFIX + name + PLACEHOLDER_SUFFIX, value);
        return this;
    }

    /**
     * W zwróconym stringu zaminia wystapienia według wartosci klucz - wartosc dodanych do placeholdera.
     *
     * @param val wartosc zaminiana
     * @return zamianione wartosci
     */
    public String replace(String val) {
        for (String key : mailPlaceholders.keySet()) {
            val = val.replace(key, mailPlaceholders.get(key));
        }
        return val;
    }
}
