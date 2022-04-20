package io.zhenye.jsonpath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonPathUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object parse(Object object, String param) throws JsonProcessingException {
        String str = mapper.writeValueAsString(object);
        Configuration config = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        return JsonPath.using(config).parse(str).read(param);
    }

    public static void set(Object object, String param) throws JsonProcessingException {
        String str = mapper.writeValueAsString(object);
        Configuration config = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        JsonPath.using(config).parse(str).set(param, "");
    }

}
