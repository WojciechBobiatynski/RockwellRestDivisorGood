package pl.sodexo.it.gryf.model.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Isolution on 2016-12-07.
 */
@ToString
public class FinanceNoteResult {

    @Getter
    @Setter
    private Long invoiceId;

    @Getter
    @Setter
    private String invoiceNumber;

    @Getter
    @Setter
    private String invoiceType;

    @Getter
    @Setter
    private Date invoiceDate;

}
