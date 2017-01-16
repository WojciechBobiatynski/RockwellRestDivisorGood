package pl.sodexo.it.gryf.service.local.api;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface FileService {

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
    String writeFile(FileType fileType, String fileName, FileDTO fileDTO, AuditableEntity entity);

    /**
     * Zapisuje plik do podanego roota
     * @param fileDTO - obiekt transferowy pliku
     * @param newFileRoot - root
     * @return nowa ścieżka do pliku
     */
    String writeFile(FileDTO fileDTO, String newFileRoot);

    /**
     * Usuwa plik
     * @param fileName pełna ścieżka do pliku
     */
    void deleteFile(String fileName);

    /**
     * Kasuje zapisane pliki. Metoda wykorzystywana w przypadku wystąpienia błędu.
     * Pliki są zapisane jeżeli mają ustawione pole fileLocation.
     * @param files lista plików
     */
    void deleteSavedFiles(List<FileDTO> files);

    /**
     * Zwraca InputStream dla danej sciezki do pliku
     * @param fileName sciezka do pliku
     * @return stromień do czytania
     */
    InputStream getInputStream(String fileName);

    /**
     * Zwraca OutputStream dla danej sciezki do pliku
     * @param fileName sciezka do pliku
     * @return stromień do czytania
     */
    OutputStream getOutputStream(String fileName);

    /**
     * Na podstawie typu pliku. Zwraca ściezkę do pliku.
     * @param fileType typ pliku
     * @return ścieżka do pliku
     */
    String findPath(FileType fileType);

    /**
     * Kopiuje plik ze ścieżki źródłowej do podanego roota
     * @param sourceFilePath - oryginalna ścieżka
     * @param newFileRoot - root
     * @return nowa ścieżka do pliku
     */
    String copyFile(String sourceFilePath, String newFileRoot);
}
