package pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.VersionableDto;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform.ZipCodeDto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-26.
 */
@ToString
public class EnterpriseDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String accountPayment;

    @Getter
    @Setter
  //  @Pattern(message = "Numer bankowy musi zawierać 26 cyfr", regexp = "^[0-9]{26}$")
    private String accountRepayment;

    @Getter
    @Setter
   // @NotEmpty(message = "Nazwa nie może być pusta")
    private String name;

    @Getter
    @Setter
  //  @NotEmpty(message = "NIP nie może być pusty")
   // @VatRegNumFormat(message = "Błedny format NIP")
    private String vatRegNum;

    @Getter
    @Setter
 //   @NotEmpty(message = "Adres do faktury nie może być pusty")
    private String addressInvoice;

    @Getter
    @Setter
    @NotNull(message = "Kod do faktury nie może być pusty")
    private ZipCodeDto zipCodeInvoice;

    @Getter
    @Setter
    //@NotEmpty(message = "Adres korespondencyjny nie może być pusty")
    private String addressCorr;

    @Getter
    @Setter
    @NotNull(message = "Kod korespondencyjny nie może być pusty")
    private ZipCodeDto zipCodeCorr;

    @Getter
    @Setter
    private String remarks;

    @Getter
    @Setter
    //@Valid
    //    @JsonManagedReference(Enterprise.CONTACTS_ATTR_NAME)
    private List<EnterpriseContactDto> contacts;

    //    @JsonIgnore
    //    private List<Order> orders;

}
