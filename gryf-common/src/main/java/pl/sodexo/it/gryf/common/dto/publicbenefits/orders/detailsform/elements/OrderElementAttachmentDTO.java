package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.FileContainerDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;

/**
 * Created by tomasz.bilski.ext on 2015-08-21.
 */
@ToString
public class OrderElementAttachmentDTO extends OrderElementDTO  implements FileContainerDTO {

    @JsonIgnore
    private FileDTO file;

    private String  fileName;

    private boolean fileIncluded;

    private boolean fileToDelete;

    //GETTERS & SETTERS

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isFileIncluded() {
        return fileIncluded;
    }

    public void setFileIncluded(boolean fileIncluded) {
        this.fileIncluded = fileIncluded;
    }

    public boolean isFileToDelete() {
        return fileToDelete;
    }

    public void setFileToDelete(boolean fileToDelete) {
        this.fileToDelete = fileToDelete;
    }

}
