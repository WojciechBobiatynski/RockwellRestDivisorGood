package pl.sodexo.it.gryf.model.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by akmiecinski on 08.11.2016.
 */
@Entity
@Table(name = "TE_ROLES", schema = "APP_PBE")
@ToString
public class TeRole extends GryfEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CODE")
    @Getter
    @Setter
    private String code;

    @Column(name = "DESCRIPTION")
    @Getter
    @Setter
    private String description;

    @Column(name = "CONTEXT")
    @Getter
    @Setter
    private String context;

}
