package pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by Isolution on 2016-10-27.
 */
@ToString
public class AsynchJobDetailsDTO extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long orderId;

    @Getter
    @Setter
    @NotNull(message = "Należy wybrać program dofinansowania")
    private Long grantProgramId;

    @Getter
    @Setter
    @NotNull(message = "Należy podać typ importowanych danych")
    private String type;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    @NotNull(message = "Plik nie został wybrany")
    @JsonIgnore
    private FileDTO file;

    @Getter
    @Setter
    private String filePath;

}
