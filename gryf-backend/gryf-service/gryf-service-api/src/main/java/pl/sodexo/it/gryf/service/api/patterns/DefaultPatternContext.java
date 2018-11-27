package pl.sodexo.it.gryf.service.api.patterns;

import lombok.Getter;
import lombok.Setter;

public class DefaultPatternContext implements PatternContext<Long, String, String> {

    private Long id;

    private String code;

    private String defaultPattern;


    public DefaultPatternContext(Long id, String code, String defaultPattern) {
        this.id = id;
        this.code = code;
        this.defaultPattern = defaultPattern;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDefaultPattern() {
        return defaultPattern;
    }

    /**
     * Tworzenie budowniczego klasy
     *
     * @return
     */
    public static Builder create() {
        return new Builder();
    }

    public static Builder create(Long id) {
        return new Builder(id);
    }

    public static class Builder {

        private Long id;

        private String code;

        private String defaultPattern;

        public Builder(Long id) {
            this.id = id;
        }

        public Builder() {
            //domyslny
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCode(String code) {
            this.code = code;
            return this;
        }

        public Builder withDefaultPattern(String defaultPattern) {
            this.defaultPattern = defaultPattern;
            return this;
        }

        /**
         * Weryfikuje poprawność budowanego obiektu wyjątku. Jeżeli obiekt nie jest poprawny to zostnie
         * rzucione błąd.
         * Jeżeli nie występuje kod wiadomości to zostanie on utworzony.
         *
         * @return obiekt wyjątku
         */
        public PatternContext build() {
            return new DefaultPatternContext(id, code, defaultPattern);
        }
    }
}
