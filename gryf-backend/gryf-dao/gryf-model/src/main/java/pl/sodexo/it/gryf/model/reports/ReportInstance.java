package pl.sodexo.it.gryf.model.reports;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.CreationAuditedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by adziobek on 07.12.2016.
 */
@ToString
@Entity
@Table(name = "REPORT_INSTANCES", schema = "APP_PBE")
public class ReportInstance extends CreationAuditedEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    @Getter
    @Setter
    private Long id;

    @Column(name = "TEMPLATE_NAME")
    @Getter
    @Setter
    private String templateName;

    @Column(name = "PARAMETERS")
    @Getter
    @Setter
    private String parameters;

    @Column(name = "PATH")
    @Getter
    @Setter
    private String path;

    @Column(name = "SOURCE_TYPE")
    @Getter
    @Setter
    private String sourceType;

    @Column(name = "SOURCE_ID")
    @Getter
    @Setter
    private Long sourceId;
}