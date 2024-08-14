package by.andd3dfx.voting.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static String asJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
