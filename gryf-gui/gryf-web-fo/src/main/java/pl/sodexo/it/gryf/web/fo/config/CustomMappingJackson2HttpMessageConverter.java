package pl.sodexo.it.gryf.web.fo.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;

/**
 * Konwerter Jackson2Http dla Message Convertera uwzględniający customowy Deserializer
 *
 * Created by akmiecinski on 2016-09-26.
 */
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    @Autowired
    public CustomMappingJackson2HttpMessageConverter(ApplicationContext ctx) {
        super(Jackson2ObjectMapperBuilder.json().applicationContext(ctx).build());
        ObjectMapper objectMapper = getObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule mod = new SimpleModule().addDeserializer(Object.class, JsonMapperUtils.createUntypedNumberDeserializer());
        objectMapper.registerModule(mod);
    }

}
