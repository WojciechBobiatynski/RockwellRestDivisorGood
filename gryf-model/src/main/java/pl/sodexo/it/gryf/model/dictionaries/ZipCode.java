package pl.sodexo.it.gryf.model.dictionaries;


import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.VersionableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-06-15.
 */
@ToString
@Entity
@Table(name = "ZIP_CODES", schema = "EAGLE")
public class ZipCode extends VersionableEntity {

    //STATIC FIELDS

    public static final String ID_ATTR_NAME = "id";
    public static final String STATE_ATTR_NAME = "state";
    public static final String ZIP_CODE_ATTR_NAME = "zipCode";
    public static final String CITY_NAME_ATTR_NAME = "cityName";
    public static final String ACTIVE_ATTR_NAME = "active";

    //FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "VOIVODESHIP_ID", referencedColumnName = "ID")
    @NotNull(message = "Województwo nie może być puste")
    private State state;

    @Column(name = "ZIP_CODE")
    @NotEmpty(message = "Kod pocztowy nie może być pusty")
    private String zipCode;

    @Column(name = "CITY_NAME")
    @NotEmpty(message = "Miejscowość nie może być pusta")
    private String cityName;

    @Column(name = "ACTIVE")
    @NotNull(message = "Aktywny nie może być pusty")
    private Boolean active;

    //GETTERS & SETTERS

    public State getState() {
        return state;
    }

    public void setState(State state) {
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

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((ZipCode)o).id);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
