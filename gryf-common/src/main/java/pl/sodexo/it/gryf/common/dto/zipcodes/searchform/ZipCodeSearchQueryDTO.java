package pl.sodexo.it.gryf.common.dto.zipcodes.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

/**
 * Created by tomasz.bilski.ext on 2015-06-17.
 */
@ToString
public class ZipCodeSearchQueryDTO extends SearchDto {

    //FIELDS

    private String zipCode;

    private  String cityName;

    private Boolean active;

    private Long stateId;

    //GETTERS & SETTERS

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

}
