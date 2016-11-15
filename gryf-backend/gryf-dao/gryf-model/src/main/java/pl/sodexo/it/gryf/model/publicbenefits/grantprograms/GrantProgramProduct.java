package pl.sodexo.it.gryf.model.publicbenefits.grantprograms;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProduct;
import pl.sodexo.it.gryf.model.stock.products.Product;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = {"product", "program"})
@Entity
@Table(name = "GRANT_PROGRAM_PRODUCTS", schema = "APP_PBE")
@NamedQueries(
        {@NamedQuery(name = GrantProgramProduct.FIND_BY_GRANT_PROGRAM_IN_DATE, query="select distinct gpp from GrantProgramProduct gpp " +
                                                                            "where gpp.program.id = :grantProgramId " +
                                                                            "and (gpp.dateFrom is null or gpp.dateFrom <= :date) " +
                                                                            "and (gpp.dateTo is null or :date <= gpp.dateTo)")})
public class GrantProgramProduct extends GryfEntity {

    
    public static final String FIND_BY_GRANT_PROGRAM_IN_DATE = "GrantProgramProduct.findByGrantProgramInDate";    
    
    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    private GrantProgram program;

    @ManyToOne
    @JoinColumn(name = "PRD_ID", referencedColumnName = "ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "PBE_PRD_ID", referencedColumnName = "PRD_ID")
    private PbeProduct pbePproduct;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantProgram getProgram() {
        return program;
    }

    public void setProgram(GrantProgram program) {
        this.program = program;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PbeProduct getPbePproduct() {
        return pbePproduct;
    }

    public void setPbePproduct(PbeProduct pbePproduct) {
        this.pbePproduct = pbePproduct;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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
        return Objects.equals(id, ((GrantProgramProduct) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
