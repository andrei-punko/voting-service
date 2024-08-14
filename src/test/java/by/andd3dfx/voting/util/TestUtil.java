package by.andd3dfx.voting.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

public class TestUtil {

    @Getter
    private final static ObjectMapper mapper = new ObjectMapper();

    public static String asJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
