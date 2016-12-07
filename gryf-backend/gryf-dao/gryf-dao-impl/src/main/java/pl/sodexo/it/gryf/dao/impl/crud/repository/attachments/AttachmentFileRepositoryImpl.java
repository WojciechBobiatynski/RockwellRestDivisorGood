package pl.sodexo.it.gryf.dao.impl.crud.repository.attachments;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.attachments.AttachmentFileRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.attachments.AttachmentFile;

/**
 * Implementacja repozytorium dla słownika z typami załączników
 *
 * Created by akmiecinski on 07.12.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AttachmentFileRepositoryImpl extends GenericRepositoryImpl<AttachmentFile, Long> implements AttachmentFileRepository {

}