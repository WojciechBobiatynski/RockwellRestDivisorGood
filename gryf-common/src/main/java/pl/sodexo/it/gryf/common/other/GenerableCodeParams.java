package pl.sodexo.it.gryf.common.other;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa przetrzymująca parametry dla tworzenia kodu typu użytkownika (OsFiz/MŚP)
 *
 * Created by akmiecinski on 05.12.2016.
 */
@ToString
public class GenerableCodeParams {

    @Getter
    @Setter
    private String prefix;

    @Getter
    @Setter
    private int zeroCount;

}
