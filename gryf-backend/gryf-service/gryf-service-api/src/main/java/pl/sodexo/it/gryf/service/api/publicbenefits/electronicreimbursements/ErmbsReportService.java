package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;

/**
 * Serwis do operacji na raportach rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
public interface ErmbsReportService {

    /**
     * Pobiera raport załącznik na podstawie ID
     * @param id - id raportu
     * @return - dto pliku raportu
     */
    FileDTO getErmbsReportFileById(Long id);

}
