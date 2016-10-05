package pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationVersionDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantApplicationActionService {

    List<GrantApplicationSearchResultDTO> findApplications(GrantApplicationSearchQueryDTO searchDTO);

    String findApplicationFormData(Long id);

    Long saveApplication(Long versionId, String data, List<FileDTO> fileDtoList);

    Long updateApplication(Long versionId, String data, List<FileDTO> fileDtoList);

    Long applyApplication(Long versionId, String data, List<FileDTO> fileDtoList, List<String> acceptedViolationsList);

    Long executeApplication(Long id, String data, List<FileDTO> fileDtoList, boolean checkVatRegNumDup);

    Long rejectApplication(Long id, String data, List<FileDTO> fileDtoList);

    FileDTO getApplicationAttachmentFile(Long attachmentId);

    void manageLocking(Long id);

    void manageLocking(Long id, String message);

    List<DictionaryDTO> FindGrantProgramsDictionaries();

    List<GrantApplicationVersionDictionaryDTO> findGrantApplicationVersionsDictionaries(Long grantProgramId);

}
