package pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.validation.VatRegNumFormat;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-26.
 */
public class EnterpriseDto {

    private Long id;

    private String code;

    private String accountPayment;

    @Pattern(message = "Numer bankowy musi zawierać 26 cyfr", regexp = "^[0-9]{26}$")
    private String accountRepayment;

    @NotEmpty(message = "Nazwa nie może być pusta")
    private String name;

    @NotEmpty(message = "NIP nie może być pusty")
    @VatRegNumFormat(message = "Błedny format NIP")
    private String vatRegNum;

    @NotEmpty(message = "Adres do faktury nie może być pusty")
    private String addressInvoice;

    @NotNull(message = "Kod do faktury nie może być pusty")
    private ZipCode zipCodeInvoice;

    @NotEmpty(message = "Adres korespondencyjny nie może być pusty")
    private String addressCorr;

    @NotNull(message = "Kod korespondencyjny nie może być pusty")
    private ZipCode zipCodeCorr;

    @Column(name = "REMARKS")
    private String remarks;

    @Valid
    @JsonManagedReference(Enterprise.CONTACTS_ATTR_NAME)
    private List<EnterpriseContact> contacts;

    @JsonIgnore
    private List<Order> orders;

}
