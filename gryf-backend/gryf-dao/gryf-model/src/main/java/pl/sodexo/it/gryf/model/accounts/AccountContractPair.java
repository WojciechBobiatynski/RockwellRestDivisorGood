package pl.sodexo.it.gryf.model.accounts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import javax.persistence.*;

/**
 * Created by akmiecinski on 02.11.2016.
 */
@Entity
@Table(name = "ACCOUNT_CONTRACT_PAIRS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = "AccountContractPair.findByAccountPayment", query = "select a from AccountContractPair a where a.accountPayment = :accountPayment"),
        @NamedQuery(name = "AccountContractPair.findByContractId", query = "select a from AccountContractPair a where a.contractId = :contractId")
})
@ToString(exclude = "grantProgram")
public class AccountContractPair extends VersionableEntity {

    public static final String FIND_BY_ACCOUNT_PAYMENT = "AccountContractPair.findByAccountPayment";
    public static final String FIND_BY_CONTRACT_ID = "AccountContractPair.findByContractId";

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    @Getter
    @Setter
    private Long id;

    @Column(name = "CONTRACT_ID")
    @Getter
    @Setter
    private Long contractId;

    @Column(name = "ACCOUNT_PAYMENT")
    @Getter
    @Setter
    private String accountPayment;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID")
    @Getter
    @Setter
    private GrantProgram grantProgram;

    @Column(name = "USED")
    @Getter
    @Setter
    private boolean used;

}
