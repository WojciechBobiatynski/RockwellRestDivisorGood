package pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationAttachment;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;

/**
 * Created by tomasz.bilski.ext on 2015-07-09.
 */
@Repository
public class GrantApplicationAttachmentRepositoryImpl extends GenericRepositoryImpl<GrantApplicationAttachment, Long> implements GrantApplicationAttachmentRepository {
}