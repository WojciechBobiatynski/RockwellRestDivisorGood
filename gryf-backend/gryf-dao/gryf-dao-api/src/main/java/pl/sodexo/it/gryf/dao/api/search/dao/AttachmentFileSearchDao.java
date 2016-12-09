package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.attachments.AttachmentFileDto;

import java.util.List;

/**
 * Dao dla plików załączników
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface AttachmentFileSearchDao {

    /**
     * Zwraca wszystkie pliki oznaczone jako do usunięcia oraz niepowiązane z odpowiednimi relacjami ze względu na niespodziewane błędy
     * @return dto plików
     */
    List<AttachmentFileDto> findAttachmentsToDelete();

}
