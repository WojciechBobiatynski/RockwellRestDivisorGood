package pl.sodexo.it.gryf.common.dto.publicbenefits;

import lombok.ToString;

/**
 * Created by tomasz.bilski.ext on 2015-09-18.
 */
@ToString
public class MailAttachmentDTO {

    //FIELDS

    private String path;

    private String name;

    //GETTERS & SETTERS

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
