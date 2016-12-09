package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.attachments.AttachmentFileDto;

import java.util.List;

/**
 * Mapper dla plików załączników
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface AttachmentFileSearchMapper {

    /**
     * Zwraca wszystkie pliki oznaczone jako do usunięcia oraz niepowiązane z odpowiednimi relacjami ze względu na niespodziewane błędy
     * @param criteria - kryteria użytkownika
     * @return dto plików
     */
    List<AttachmentFileDto> findAttachmentsToDelete(@Param("criteria") UserCriteria criteria);

}
