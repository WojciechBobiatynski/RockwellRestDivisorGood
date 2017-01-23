package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Isolution on 2016-11-02.
 */
@Entity
@ToString(exclude = {"product", "grantProgram"})
@Table(name = "PBE_PRODUCT_EMISSIONS", schema = "APP_PBE")
public class PbeProductEmission extends GryfEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @JoinColumn(name = "PRD_ID", referencedColumnName = "PRD_ID")
    @ManyToOne
    private PbeProduct product;

    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    @ManyToOne
    private GrantProgram grantProgram;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EMISSION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emissionDate;

    @Column(name = "QUANTITY")
    private Integer quantity;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PbeProduct getProduct() {
        return product;
    }

    public void setProduct(PbeProduct product) {
        this.product = product;
    }

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(Date emissionDate) {
        this.emissionDate = emissionDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
