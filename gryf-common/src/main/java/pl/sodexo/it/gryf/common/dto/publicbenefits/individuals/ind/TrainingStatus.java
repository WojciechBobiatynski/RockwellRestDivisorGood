package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;

public enum TrainingStatus {

    RESERVED("Zarezerwowane"),
    SETTLED("Rozliczone");

    @Getter
    @Setter
    private String name;

    TrainingStatus(String name) {
        this.name = name;
    }
}