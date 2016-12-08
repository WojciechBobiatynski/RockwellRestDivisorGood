package pl.sodexo.it.gryf.service.api.asynchjobs;

import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;

public interface AsynchJobFileService {

    String saveFile(AsynchJobDetailsDTO createDTO);
}