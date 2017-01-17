package pl.sodexo.it.gryf.common.dto.asynchjobs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Isolution on 2016-12-06.
 */
@ToString
public class AsynchronizeJobResultInfoDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private String description;

    public AsynchronizeJobResultInfoDTO(Long id) {
        this.id = id;
    }

    public AsynchronizeJobResultInfoDTO(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public AsynchronizeJobResultInfoDTO(Long id, String status, String description) {
        this.id = id;
        this.status = status;
        this.description = description;
    }
}
