package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;

import java.util.List;

/**
 * Mapper batisowy dla instacji produktów
 *
 * Created by jbentyn on 2016-10-17.
 */
public interface ProductInstanceSearchMapper {

    /**
     * Zwraca liczbe bonów, które sa gotowe do generowania numeru drukowanego
     *
     * @param productId - unikatowy numer produktu
     * @return liczba bonów gotowych do generacji numeru
     */
    Long countAvailableToNumberGeneration(@Param("productId") String productId);

    /**
     * Zaciąga paczkę PrintNumberDto do generowania numerów przez proces batchowy do generowania numerów drukowanych bonów
     *
     * @param productId - numer produktu
     * @param pagesize - wielkośc zaciąganej paczki, konfigurowana w xml
     * @return - lista PrintNumberDto do wygenerowania numerów drukowanych
     */
    List<PrintNumberDto> getToGenerateNumber(@Param("productId") String productId, @Param("_pagesize") Integer pagesize);

    /**
     * Update wykorzystywany przez proces batchowy do generowania numerów drukowanych bonów
     *
     * @param printNumberDto - dto numeru drukowanego z wygenerowanym numerem drukowanym oraz CRC
     */
    void updatePrintNumber(@Param("printNumberDto") PrintNumberDto printNumberDto);
}
