package pl.sodexo.it.gryf.web.common.util;

import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.common.exception.GryfUploadException;
import pl.sodexo.it.gryf.common.utils.GryfUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tomasz.bilski.ext on 2015-08-21.
 */
public final class WebUtils {

    private WebUtils() {
    }

    public static List<FileDTO> createFileDtoList(MultipartFile[] files) throws IOException {
        List<FileDTO> result = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            FileDTO fileDTO = createFileDto(file);
            result.add(fileDTO);
        }
        return result;
    }

    private static FileDTO createFileDto(MultipartFile file) throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setName(file.getName());
        fileDTO.setOriginalFilename(file.getOriginalFilename());
        fileDTO.setContentType(file.getContentType());
        fileDTO.setIsEmpty(file.isEmpty());
        fileDTO.setSize(file.getSize());
        fileDTO.setInputStream(file.getInputStream());
        return fileDTO;
    }

    public static AsynchJobDetailsDTO fillAsynchJobDtoWithAttachment(Map<String, MultipartFile> fileMap, AsynchJobDetailsDTO source) {
        if(fileMap.size() > 0) {
            try {
                source.setFile(createFileDto((MultipartFile) fileMap.values().toArray()[0]));
            } catch (IOException e) {
                throw new GryfUploadException("Nie udało się zuploadować plików", e);
            }
        }
        return source;
    }

    public static ElctRmbsHeadDto fillErmbsDtoWithAttachments(Map<String, MultipartFile> fileMap, ElctRmbsHeadDto source) {
        source.getAttachments().stream().filter(ErmbsAttachmentDto::isChanged).forEach(ermbsAttachmentsDto -> {
            MultipartFile multipartFile = fileMap.get("file[" + ermbsAttachmentsDto.getIndex() + "]");
            try {
                ermbsAttachmentsDto.setFile(createFileDto(multipartFile));
            } catch (IOException e) {
                throw new GryfUploadException("Nie udało się zuploadować plików", e);
            }
        });
        return source;
    }

    public static void writeFileToResponse(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String fileName) throws IOException {
        //MIME TYPE
        ServletContext context = request.getServletContext();
        String mimeType = context.getMimeType(fileName);
        response.setContentType((mimeType == null) ? "application/octet-stream" : mimeType);

        //HEADER
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

        //COPY STREAM
        OutputStream outStream = response.getOutputStream();
        GryfUtils.copyStream(inputStream, outStream);

        inputStream.close();
        outStream.close();
    }

}
