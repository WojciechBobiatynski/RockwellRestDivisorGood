package pl.sodexo.it.gryf.web.ind.dto;

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