package pl.sodexo.it.gryf.common.utils;

import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumTrainingInstitutionExistException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * Glasa zbierajaca ogólne funkcjionalności w całej aplikacji.
 */
public final class GryfUtils {

    private static final int BUFFER_SIZE = 8192;

    private GryfUtils() {}

    public static void checkForUpdate(Object requestId, Object objectId){
        if(requestId == null){
            throw new RuntimeException("Nie przekazano id w URL dla zmiany");
        }
        if(objectId == null){
            throw new RuntimeException("Nie przekazano id w zmienianym objekcie");
        }
        if (!requestId.equals(objectId)) {
            throw new RuntimeException(String.format("Id w URL [%s] jest inne od id w objekcie [%s]",  requestId, objectId));
        }
    }

    public static Date getStartDay(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getEndDay(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date getStartMonth(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return getStartDay(c.getTime());
    }

    public static Date getEndMonth(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return getEndDay(c.getTime());
    }

    public static Date getNextMonth(Date date){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        return getEndDay(c.getTime());
    }

    public static Date addDays(Date date, int days){
        if(date == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    public static long copyStream(InputStream source, OutputStream sink) throws IOException {
        long nRead = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
            nRead += n;
        }
        return nRead;
    }

    public static VatRegNumTrainingInstitutionExistException rethrowException(VatRegNumTrainingInstitutionExistException e, String s) {
        throw new VatRegNumTrainingInstitutionExistException(s + e.getMessage(), e.getTrainingInstitutions());
    }

    public static void rethrowException(EntityValidationException e, String s) {
        for (EntityConstraintViolation v : e.getViolations()) {
            v.setMessage(s + v.getMessage());
        }
        throw new EntityValidationException(e.getViolations());
    }

    public static String amountToString(BigDecimal value){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00#######", symbols);
        return formatter.format(value);
    }

    /**
     * Gdy wartośc jest null zwraca zero, w przeciwnym wypadku daną wartość
     * @param value wartosc
     * @return zero lub wartosc
     */
    public static BigDecimal getValue(BigDecimal value){
        return value != null ? value : BigDecimal.ZERO;
    }

    /**
     * Metoda zwraca sformatowany string z adresami email na podstawie zbioru adresów e-mail
     * @param recipientsSet zbiór adresów email odbiorców
     * @return sformatowany string odbiorców, adresy przedzielone przecinkiem
     */
    public static String formatEmailRecipientsSet(Set<String> recipientsSet){
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> i = recipientsSet.iterator(); i.hasNext();){
            sb.append(i.next());
            if(i.hasNext()){
                sb.append(",");
            }
        }
        return sb.toString();        
    }

    public static boolean isAssignableFrom(Class clazz, Class[] classes){
        for (int i = 0; i < classes.length; i++) {
            if(clazz.isAssignableFrom(classes[i])){
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(Set<?> set){
        return set == null || set.isEmpty();
    }

    public static boolean isEmpty(List<?> list){
        return list == null || list.isEmpty();
    }

    public static <T> List<T> filter(List<T> list, Predicate<? super T> predicate){
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if(predicate.apply(t)){
                result.add(t);
            }
        }
        return result;
    }

    public static <T> T find(List<T> list, Predicate<? super T> predicate){
        for (T t : list) {
            if(predicate.apply(t)){
                return t;
            }
        }
        return null;
    }

    public static <T> boolean contain(List<T> list, Predicate<? super T> predicate){
        for (T t : list) {
            if(predicate.apply(t)){
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contain(T[] table, Predicate<? super T> predicate){
        for (T t : table) {
            if(predicate.apply(t)){
                return true;
            }
        }
        return false;
    }

    public static <T> int countOccurrence(List<T> list, Predicate<? super T> predicate){
        int occurrence = 0;
        for (T t : list) {
            if(predicate.apply(t)){
                occurrence ++;
            }
        }
        return occurrence;
    }

    public static <K, T> Map<K, T> constructMap(List<T> list, MapConstructor<K, T>  mapConstructor){
        Map<K, T> result = new HashMap<>();
        for (T t : list) {
            if(mapConstructor.isAddToMap(t)){
                result.put(mapConstructor.getKey(t), t);
            }
        }
        return result;
    }

    public static <T, R> List<R> constructList(List<T> list, ListConstructor<? super T, R> action){
        List<R> result = new ArrayList<>();
        for (T t : list) {
            if(action.isAddToList(t)) {
                result.add(action.create(t));
            }
        }
        return result;
    }

    public static <T, R> Set<R> constructSet(List<T> list, SetConstructor<? super T, R> action){
        Set<R> result = new HashSet<>();
        for (T t : list) {
            if(action.isAddToSet(t)) {
                result.add(action.create(t));
            }
        }
        return result;
    }

    public interface Predicate<T>{
        boolean apply(T input);
    }

    public interface MapConstructor<K, T>{
        boolean isAddToMap(T input);
        K getKey(T input);
    }

    public interface ListConstructor<T, R>{
        boolean isAddToList(T input);
        R create(T input);
    }

    public interface SetConstructor<T, R>{
        boolean isAddToSet(T input);
        R create(T input);
    }

    public static String convertBooleanToDatabaseColumn(Boolean b) {
        return (b != null && b) ? GryfConstants.FLAG_TRUE : GryfConstants.FLAG_FALSE;
    }

    public static Boolean convertDatabaseColumnToBoolean(String s) {
        return GryfConstants.FLAG_TRUE.equals(s);
    }

}
