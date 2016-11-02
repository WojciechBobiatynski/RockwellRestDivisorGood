package pl.sodexo.it.gryf.model.publicbenefits.grantprograms;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString
@Entity
@Table(name = "GRANT_OWNERS", schema = "APP_PBE")
public class GrantOwner extends GryfEntity {

    public static final String NAME_ATTR_NAME = "name";

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL_ADDRESSES_GRANT_APP_INFO")
    private String emailAddressesGrantAppInfo;
            
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

    public String getEmailAddressesGrantAppInfo() {
        return emailAddressesGrantAppInfo;
    }

    public void setEmailAddressesGrantAppInfo(String emailAddressesGrantAppInfo) {
        this.emailAddressesGrantAppInfo = emailAddressesGrantAppInfo;
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
        return Objects.equals(id, ((GrantOwner) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}
