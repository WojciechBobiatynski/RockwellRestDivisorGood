package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantOwner;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-11-02.
 */
@Entity
@ToString(exclude = {"grantOwner"})
@Table(name = "PBE_PRODUCTS", schema = "APP_PBE")
public class PbeProduct extends GryfEntity{

    @Id
    @Column(name = "PRD_ID")
    private String id;

    @Column(name = "PRD_TYPE")
    private String productType;

    @JoinColumn(name = "GRANT_OWNER_ID", referencedColumnName = "ID")
    @ManyToOne
    private GrantOwner grantOwner;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private BigDecimal value;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    @ManyToOne
    @Setter
    @Getter
    private GrantProgram grantProgram;

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public GrantOwner getGrantOwner() {
        return grantOwner;
    }

    public void setGrantOwner(GrantOwner grantOwner) {
        this.grantOwner = grantOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
