package pl.sodexo.it.gryf.service.impl.dictionaries;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ContactTypeRepository;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.service.api.dictionaries.ContactTypeService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.ContactTypeEntityMapper;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-19.
 */
@Service
@Transactional
public class ContactTypeServiceImpl implements ContactTypeService {

    //PRIVATE FIELDS

    @Autowired
    private ContactTypeRepository contactTypeRepository;

    @Autowired
    private ContactTypeEntityMapper contactTypeEntityMapper;

    //PUBLIC METHODS

    @Override
    @Cacheable(cacheName = "contactTypeList")
    public List<ContactTypeDto> findContactTypes() {
        List<ContactType> contactTypes = contactTypeRepository.findAll();
        return contactTypeEntityMapper.convert(contactTypes);
    }



}

