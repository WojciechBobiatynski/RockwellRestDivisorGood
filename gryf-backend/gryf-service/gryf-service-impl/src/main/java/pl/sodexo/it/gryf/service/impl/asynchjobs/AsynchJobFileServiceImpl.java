package pl.sodexo.it.gryf.service.impl.asynchjobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.service.api.asynchjobs.AsynchJobFileService;
import pl.sodexo.it.gryf.service.local.api.FileService;

@Service
@Transactional
public class AsynchJobFileServiceImpl implements AsynchJobFileService {

    @Autowired
    FileService fileService;

    @Override
    public String saveFile(AsynchJobDetailsDTO createDTO) {
        String fileName = getNewFileName(createDTO);
        return fileService.writeFile(FileType.DATA_IMPORT, fileName, createDTO.getFile(), null);
    }

    private String getNewFileName(AsynchJobDetailsDTO createDTO) {
        String fileName = createDTO.getFile().getOriginalFilename() + "_" +  createDTO.getId();
        return GryfStringUtils.convertFileName(fileName);
    }

}
