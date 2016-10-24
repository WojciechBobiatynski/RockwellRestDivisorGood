package pl.sodexo.it.gryf.web.fo.utils;

import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-21.
 */
public final class WebUtils {

    private WebUtils() {}

    public static  List<FileDTO> createFileDtoList(MultipartFile[] files) throws IOException {
        List<FileDTO> result = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];

            FileDTO fileDTO = new FileDTO();
            fileDTO.setName(file.getName());
            fileDTO.setOriginalFilename(file.getOriginalFilename());
            fileDTO.setContentType(file.getContentType());
            fileDTO.setIsEmpty(file.isEmpty());
            fileDTO.setSize(file.getSize());
            fileDTO.setInputStream(file.getInputStream());
            result.add(fileDTO);
        }
        return result;
    }
}
