package pl.sodexo.it.gryf.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.model.AuditableEntity;
import pl.sodexo.it.gryf.model.FileType;
import pl.sodexo.it.gryf.utils.GryfUtils;
import pl.sodexo.it.gryf.utils.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tomasz.bilski.ext on 2015-07-09.
 */
@Service
@Transactional
public class FileService {

    //FIELDS

    @Autowired
    private ApplicationParametersService parameterService;

    //PUBLIC METHODS

    /**
     * Zapisuje plik danego typu na dysku.
     * @param fileType typ pliku
     * @param fileName nazwa pliku
     * @param fileDTO obiekt transferowy pliku
     * @param entity obiekt z kolumną modifiedTimestamp, gdy zapisujemy plik i plik ma taką samą nazwę jak poporzedni plik
     *               to rekord trzymajacy nazwę pliku nie zauważy tej zmiany (nazwa pliku w kolumnie sie nie zmieni) dlatego
     *               przekazujemy tu obiekt AuditableEntity i ustawiamy aktualną datę w polu ModifiedTimestamp (pole może być null)
     * @return pełna ścieżka do pliku
     */
    public String writeFile(FileType fileType, String fileName, FileDTO fileDTO, AuditableEntity entity) {
        String filePath = findFileLocation(fileType, fileName, fileDTO);

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            InputStream is = fileDTO.getInputStream();
            GryfUtils.copyStream(is, fos);
            is.close();
            fos.close();
            fileDTO.setFileLocation(filePath);
            if(entity != null) {
                entity.setModifiedTimestamp(new Date());
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Nie udało się zapisać pliku [%s]", filePath), e);
        }
        return filePath;
    }

    /**
     * Usuwa plik
     * @param fileName pełna ścieżka do pliku
     */
    public void deleteFile(String fileName) {
        if(!StringUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            file.delete();
        }
    }

    /**
     * Kasuje zapisane pliki. Metoda wykorzystywana w przypadku wystąpienia błędu.
     * Pliki są zapisane jeżeli mają ustawione pole fileLocation.
     * @param files lista plików
     */
    public void deleteSavedFiles(List<FileDTO> files){
        for (FileDTO file : files) {
            if(!StringUtils.isEmpty(file.getFileLocation())){
                try {
                    deleteFile(file.getFileLocation());
                }catch(RuntimeException e){
                    Logger.getLogger(FileService.class.getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Zwraca InputStream dla danej sciezki do pliku
     * @param fileName sciezka do pliku
     * @return stromień do czytania
     */
    public InputStream getInputStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Nie znależiono pliku o nazwie '%s'", fileName));
        }
    }

    /**
     * Zwraca OutputStream dla danej sciezki do pliku
     * @param fileName sciezka do pliku
     * @return stromień do czytania
     */
    public OutputStream getOutputStream(String fileName) {
        try {
            return new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Nie znależiono pliku o nazwie '%s'", fileName));
        }
    }

    /**
     * Na podstawie typu pliku. Zwraca ściezkę do pliku.
     * @param fileType typ pliku
     * @return ścieżka do pliku
     */
    public String findPath(FileType fileType) {
        switch (fileType) {
            case GRANT_APPLICATIONS:
                return parameterService.getPathAttachments() + parameterService.getPathGrantApp();
            case ORDERS:
                return parameterService.getPathAttachments() + parameterService.getPathOrders();
            case REPORT_TEMPLATES:
                return parameterService.getPathAttachments() + parameterService.getPathReportTemplates();
            case REIMBURSEMENTS:
                return parameterService.getPathAttachments() + parameterService.getPathReimbursements();
            default:
                throw new RuntimeException(String.format("Nieznany typ pliku: [%s]", fileType));
        }
    }

    //PRIVATE METHODS

    private String findFileLocation(FileType fileType, String fileName, FileDTO fileDTO) {
        StringBuffer sb = new StringBuffer();
        sb.append(findPath(fileType)).append(fileName);
        String fileExtension = StringUtils.findFileExtension(fileDTO.getOriginalFilename());
        if (!StringUtils.isEmpty(fileExtension)) {
            sb.append(".").append(fileExtension);
        }
        return sb.toString();
    }

}