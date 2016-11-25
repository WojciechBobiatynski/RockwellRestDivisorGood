package pl.sodexo.it.gryf.dao.impl.crud.repository.attachments;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.attachments.AttachmentTypeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.attachments.AttachmentType;

/**
 * Implementacja repozytorium dla słownika typów załączników
 *
 * Created by akmiecinski on 25.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AttachmentTypeRepositoryImpl extends GenericRepositoryImpl<AttachmentType, String> implements AttachmentTypeRepository {

}