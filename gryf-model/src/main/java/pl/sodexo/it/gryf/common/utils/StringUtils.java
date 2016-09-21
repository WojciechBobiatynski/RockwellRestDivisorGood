package pl.sodexo.it.gryf.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa zbierajaca ogólne funkcjionalności w całej aplikacji.
 * Klasa dostarcza metody na stringach.
 */
public abstract class StringUtils{

    //STTAIC FIELDS

    private static final Map<Character, Character> polishReplacementMap = new HashMap<>();

    static{
        polishReplacementMap.put('ą', 'a');
        polishReplacementMap.put('ć', 'c');
        polishReplacementMap.put('ę', 'e');
        polishReplacementMap.put('ł', 'l');
        polishReplacementMap.put('ń', 'n');
        polishReplacementMap.put('ó', 'o');
        polishReplacementMap.put('ś', 's');
        polishReplacementMap.put('ź', 'z');
        polishReplacementMap.put('ż', 'z');
        polishReplacementMap.put('Ą', 'A');
        polishReplacementMap.put('Ć', 'C');
        polishReplacementMap.put('Ę', 'E');
        polishReplacementMap.put('Ł', 'L');
        polishReplacementMap.put('Ń', 'N');
        polishReplacementMap.put('Ó', 'O');
        polishReplacementMap.put('Ś', 'S');
        polishReplacementMap.put('Ź', 'Z');
        polishReplacementMap.put('Ż', 'Z');
    }

    //PUBLIC METHODS

    /**
     * Metoda sprawdza czy dany ciąg znaków jest pusty null lub zerowej długości
     * @param value sprawdzany string
     * @return true gdy vartosc jest pusta
     */
    public static boolean isEmpty(String value){
        return value == null || value.trim().isEmpty();
    }

    /**
     * NullSafety to substring
     * @param o argument
     * @param beginIndex poczatek
     * @param endIndex koniec
     * @return podciąg
     */
    public static String substring(String o, int beginIndex, int endIndex){
        if(o != null){
            if(o.length() > endIndex){
                return o.substring(beginIndex, endIndex);
            }
        }
        return o;
    }

    /**
     * NullSafety to string
     * @param o objekt formatowany do stringa
     * @return sformatowany string dla obiektu
     */
    public static String toString(Object o){
        return o != null ? o.toString() : null;
    }

    /**
     * Zamienia polskie znaki na alfanumeryczne odpowiedniki.
     * @param word argument z polskimi znakami
     * @return bez polskich znaków
     */
    public static String replacePolishCharacters(String word) {
        StringBuilder sb = new StringBuilder(word.length());
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            sb.append(polishReplacedCharIfPossible(c));
        }
        return sb.toString();
    }

    private static char polishReplacedCharIfPossible(char c) {
        return polishReplacementMap.containsKey(c) ? polishReplacementMap.get(c) : c;
    }

    /**
     * Metoda dla danej scieżki do pliku/danej nazwy pliku zwraca rozszeżenie.
     * @param fileName nazwa pliku lub ścieżki
     * @return rozszeżenie
     */
    public static String findFileExtension(String fileName) {
        String[] fileNameTab = fileName.split("\\.");
        if (fileNameTab.length > 0) {
            return fileNameTab[fileNameTab.length - 1];
        }
        return "";
    }

    /**
     * Metoda dla scieżki do pliku zwraca nazwe pliku. Obcina foldery.
     * @param path scieżka do pliku
     * @return nazwa pliku
     */
    public static String findFileNameInPath(String path) {
        int index = path.lastIndexOf("\\");
        if(index > 0) {
            path = path.substring(index + 1);
        }
        index = path.lastIndexOf("/");
        if(index > 0) {
            path = path.substring(index + 1);
        }
        return path;
    }

    /**
     * Metoda dla formatuje daną nazwę na nazwę pliku. Zwracana nazwa pliku ma
     * zanaki tylko alfanumeryczne, spacje zamieniona na '_', itp.
     * @param name nazwa pliku do sformatowania
     * @return sformatowana nazwa pliku
     */
    public static String convertFileName(String name){
        return StringUtils.replacePolishCharacters(name)
                        .replaceAll("[^A-Za-z0-9 _]", "")
                        .replaceAll(" ", "_")
                        .toLowerCase();
    }

}
