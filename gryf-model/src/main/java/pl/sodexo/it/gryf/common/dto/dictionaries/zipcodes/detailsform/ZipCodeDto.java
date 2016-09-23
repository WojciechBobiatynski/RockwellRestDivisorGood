package pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform;

import pl.sodexo.it.gryf.common.dto.basic.VersionableDto;

/**
 * Dto dla encji ZipCode
 *
 * Created by jbentyn on 2016-09-23.
 */
public class ZipCodeDto extends VersionableDto {

    private Long id;

    private StateDto state;

    private String zipCode;

    private String cityName;

    private Boolean active;

    public StateDto getState() {
        return state;
    }

    public void setState(StateDto state) {
        this.state = state;
    }

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "ZipCodeDto{" + "id=" + id + ", state=" + state + ", zipCode='" + zipCode + '\'' + ", cityName='" + cityName + '\'' + ", active=" + active + '}';
    }
}
