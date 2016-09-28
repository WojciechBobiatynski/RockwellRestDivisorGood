package pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-24.
 */
@ToString
public class ZipCodeSearchResultDTO extends GryfDto{

    private Long id;

    private String zipCode;

    private String cityName;

    private boolean active;

    private String stateName;

    //CONSTRUCTORS

    public ZipCodeSearchResultDTO(){
    }

    private ZipCodeSearchResultDTO(ZipCode entity) {
        this.setId(entity.getId());
        this.setZipCode(entity.getZipCode());
        this.setCityName(entity.getCityName());
        this.setActive(entity.getActive());
        this.setStateName(entity.getState().getName());
    }

    //STATIC METHODS - CREATE

    public static ZipCodeSearchResultDTO create(ZipCode entity) {
        return entity != null ? new ZipCodeSearchResultDTO(entity) : null;
    }

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
