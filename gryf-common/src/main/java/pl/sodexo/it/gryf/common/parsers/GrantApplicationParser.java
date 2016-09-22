package pl.sodexo.it.gryf.common.parsers;

import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationDTO;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-06.
 */
public class GrantApplicationParser {

    //APPLICATION DATA

    public static GrantApplicationDTO readApplicationData(String dtoClassName, String data) {
        return readApplicationData(dtoClassName, data, new ArrayList<FileDTO>());
    }

    public static GrantApplicationDTO readApplicationData(String dtoClassName,
                                                          String data, List<FileDTO> fileDtoList) {

        //RETRIVE CLASS
        Class<?> clazz;
        try {
            clazz = Class.forName(dtoClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Nie znaleziono klasy dla dto wniosku - nazwa klasy " + dtoClassName);
        }

        //PARSE APPLICATION
        GrantApplicationDTO dto = (GrantApplicationDTO) JsonMapperUtils.readValue(data, clazz);

        //MATCH WITH FILE
        int index = 0;
        for (GrantApplicationAttachmentDTO attachmentDTO : dto.getAttachments()) {
            if (attachmentDTO.getFileIncluded()) {
                attachmentDTO.setFile(fileDtoList.get(index++));
                attachmentDTO.getFile().setAttachmentName(attachmentDTO.getName());
            }
        }

        return dto;
    }

    public static String writeApplicationData(GrantApplicationDTO dto) {
        return JsonMapperUtils.writeValueAsString(dto);
    }

    //ACCEPTED VIOLATIONS

    public static List<String> readAcceptedViolations(String acceptedViolationsStr) {
        if (StringUtils.isEmpty(acceptedViolationsStr)) {
            return new ArrayList<>();
        }
        String[] acceptedViolationsTab = JsonMapperUtils.readValue(acceptedViolationsStr, String[].class);
        return new ArrayList<>(Arrays.asList(acceptedViolationsTab));
    }


}
