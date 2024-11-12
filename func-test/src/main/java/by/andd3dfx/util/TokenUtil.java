package by.andd3dfx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TokenUtil {

    public static final String EXPIRATION = "exp";

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static LocalDateTime extractTokenExpiration(String token) {
        Integer intValue = (Integer) extractFieldFromJwt(token, EXPIRATION);
        if (intValue == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(intValue), ZoneId.of("UTC"));
    }

    public static Object extractFieldFromJwt(String jwtToken, String fieldName) {
        try {
            String[] split_string = jwtToken.split("\\.");
            String base64EncodedBody = split_string[1];

            Base64.Decoder decoder = Base64.getUrlDecoder();
            String body = new String(decoder.decode(base64EncodedBody));

            HashMap map = objectMapper.readValue(body, LinkedHashMap.class);
            if (map.containsKey(fieldName)) {
                return map.get(fieldName);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
