package pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform;

import pl.sodexo.it.gryf.common.dto.basic.GryfDto;

/**
 * Dto dla encji State
 *
 * Created by jbentyn on 2016-09-23.
 */
public class StateDto extends GryfDto {

    private Long id;

    private String name;

    private Long country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "State{" + "id=" + id + ", name='" + name + '\'' + ", country=" + country + '}';
    }
}
