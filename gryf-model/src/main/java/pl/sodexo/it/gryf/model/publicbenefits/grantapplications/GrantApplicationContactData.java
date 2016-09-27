package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.AuditableEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString
@Entity
@Table(name = "GRANT_APPLICATION_CONTACT_DATA", schema = "APP_PBE")
public class GrantApplicationContactData extends AuditableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @Column(name = "CONTACT_TYPE")
    @Enumerated(value = EnumType.STRING)
    private GrantApplicationContactType contactType;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @JsonBackReference("contacts")
    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID", referencedColumnName = "ID")
    private GrantApplicationBasicData applicationBasic;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantApplicationContactType getContactType() {
        return contactType;
    }

    public void setContactType(GrantApplicationContactType contactType) {
        this.contactType = contactType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GrantApplicationBasicData getApplicationBasic() {
        return applicationBasic;
    }

    public void setApplicationBasic(GrantApplicationBasicData applicationId) {
        this.applicationBasic = applicationId;
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
        return Objects.equals(id, ((GrantApplicationContactData) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
