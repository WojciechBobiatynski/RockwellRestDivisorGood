package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.operator;

public interface OperatorUserRepository {

    /**
     * Znajduje wartość parametrów użytkownika
     * @param login login użytkownika EAGLE
     * @param key klucz identyfikujący parameter
     * @return string z wartością parametru
     */
    String findUserParameterValueByLoginAndKey(String login, String key);
}
