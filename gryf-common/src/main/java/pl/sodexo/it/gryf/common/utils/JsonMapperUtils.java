package pl.sodexo.it.gryf.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Date;

/**
 * Util class responsible for converting DTO objects to a JSON format
 *
 * Created by Michal.CHWEDCZUK.ext on 2015-07-24.
 */
public final class JsonMapperUtils {

    private JsonMapperUtils() {}

    public static String writeValueAsString(Object o) {
        try {
            ObjectMapper objectMapper = createObjectMapper();
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Nie udało się sparsować obiektu do stringa " + o.getClass().getName());
        }
    }

    public static <T> T readValue(String value, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = createObjectMapper();
            return objectMapper.readValue(value, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się odczytać obiektu typu " + clazz.getSimpleName(), e);
        }
    }

    public static UntypedObjectDeserializer createUntypedNumberDeserializer() {
        return new UntypedObjectDeserializer() {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext context) throws IOException {
                Object out = super.deserialize(jp, context);
                if (out instanceof Integer) {
                    return Long.valueOf((Integer) out);
                }
                return out;
            }

            @Override
            public Object deserializeWithType(JsonParser jp, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
                Object out = super.deserializeWithType(jp, context, typeDeserializer);
                if (out instanceof Integer) {
                    return Long.valueOf((Integer) out);
                }
                return out;
            }
        };
    }

    public static ObjectMapper createObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule mod = new SimpleModule();
        mod.addDeserializer(Object.class, createUntypedNumberDeserializer());
        mod.addDeserializer(Date.class, new JsonDateDeserializer());
        mod.addSerializer(Date.class ,new JsonDateSerializer());
        objectMapper.registerModule(mod);
        return objectMapper;
    }
}
