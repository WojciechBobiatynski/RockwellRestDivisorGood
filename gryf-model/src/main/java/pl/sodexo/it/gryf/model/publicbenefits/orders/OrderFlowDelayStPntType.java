/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Entity
@Table(name = "ORDER_FLOW_DELAY_ST_PNT_TYPES", schema = "APP_PBE")
public class OrderFlowDelayStPntType extends GryfEntity {
    public enum DelayStartingPointType{
        /**
         * Data bieżąca
         */
        SYSDATE("Data bieżąca"), 
        /**
        *  Data zamówienia
        */
        ORDERDATE("Data zamówienia"), 
        /**
        *  Data podpisania umowy
        */
        SIGNDATE("Data podpisania umowy"), 
        /**
        *  Data zawarcia umowy
        */
        CONTRDATE("Data zawarcia umowy"), 
        /**
        *  Data realizacji zamówienia
        */
        EXEDATE("Data realizacji zamówienia");
        //FIELDS
        private String label;
        //CONSTRUCTORS
        private DelayStartingPointType(String label){
            this.label = label;
        }

        //GETTERS
        public String getLabel() {
            return label;
        }
    }
    
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ID")
    @Enumerated(EnumType.STRING)
    private DelayStartingPointType id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;

    public DelayStartingPointType getId() {
        return id;
    }

    public void setId(DelayStartingPointType id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((OrderFlowDelayStPntType) o).id);
    }
    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowDelayStPntType[ id=" + id + " ]";
    }
    
}
