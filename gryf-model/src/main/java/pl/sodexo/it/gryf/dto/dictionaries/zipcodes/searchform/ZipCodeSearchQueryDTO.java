package pl.sodexo.it.gryf.dto.dictionaries.zipcodes.searchform;

import pl.sodexo.it.gryf.dto.SearchDto;

/**
 * Created by tomasz.bilski.ext on 2015-06-17.
 */
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
