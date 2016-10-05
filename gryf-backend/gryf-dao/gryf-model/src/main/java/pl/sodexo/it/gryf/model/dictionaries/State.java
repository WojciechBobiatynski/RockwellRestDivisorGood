package pl.sodexo.it.gryf.model.dictionaries;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-06-15.
 */
@ToString
@Entity
@Table(name = "STATES", schema = "EAGLE")
@NamedQueries({
        @NamedQuery(name = State.FIND_BY_COUNTRY, query = "SELECT s FROM State s where s.country = :country order by s.name")})
public class State extends GryfEntity {

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_COUNTRY = "State.findByCountry";

    //STATIC FIELDS - ATRIBUTES

    public static final String ID_ATTR_NAME = "id";
    public static final String NAME_ATTR_NAME = "name";
    public static final String COUNTRY_ATTR_NAME = "country";

    //FIELDS

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COUNTRY")
    private Long country;

    //GETTERS & SETTERS

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

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((State) o).id);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
