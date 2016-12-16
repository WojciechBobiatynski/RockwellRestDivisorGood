package pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;

import javax.validation.constraints.NotNull;
import java.util.Date;

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

    @Getter
    @Setter
    private Date startTimestamp;

    @Getter
    @Setter
    private Date stopTimestamp;

    @Getter
    @Setter
    private String durationTime;

    //PUBLIC METHODS

    public void calculateDurationTime(){
        if(startTimestamp != null && stopTimestamp != null){
            long time = stopTimestamp.getTime() - startTimestamp.getTime();
            long miliSeconds = time % 1000;
            long seconds = (time / 1000) % 60 ;
            long minutes = (time / (1000 * 60)) % 60;
            long houres = (time / (1000 * 60 * 60)) % 24;

            durationTime = miliSeconds + "ms";
            if(seconds > 0){
                durationTime = seconds + "s " + durationTime;
            }
            if(minutes > 0){
                durationTime = minutes + "m " + durationTime;
            }
            if(houres > 0){
                durationTime = houres + "h " + durationTime;
            }
        }else {
            durationTime = null;
        }
    }

}
