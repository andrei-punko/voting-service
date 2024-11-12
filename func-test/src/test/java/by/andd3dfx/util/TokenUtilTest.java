package by.andd3dfx.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import org.junit.Test;

public class TokenUtilTest {

    @Test
    public void extractFieldFromJwt() {
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibm9uX3BpaV9pZCJdLCJ1c2VyX25hbWUiOiJib2IiLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTU4NTIyOTMyOCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjIxODEwZWUxLWI5MGQtNDQ4My1hYTE1LTJlNjZkOTc4MjhlZSIsImNsaWVudF9pZCI6ImVudGl0bGVtZW50cyIsIm5vbl9waWlfaWQiOiIyZjgtNGRhIn0.5Ef4wabYaGi5Ed56kD9bLip92BDx6-WeKNfmwh7P-wI";

        String nonPiiId = (String) TokenUtil.extractFieldFromJwt(jwtToken, "non_pii_id");

        assertThat("Wrong value", nonPiiId, is("2f8-4da"));
    }

    @Test
    public void extractFieldFromJwtWhenNoNonPiiIdEncoded() {
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibm9uX3BpaV9pZCJdLCJ1c2VyX25hbWUiOiJib2IiLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTU4NTIyOTMyOCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjIxODEwZWUxLWI5MGQtNDQ4My1hYTE1LTJlNjZkOTc4MjhlZSIsImNsaWVudF9pZCI6ImVudGl0bGVtZW50cyJ9.81YA135DvLBaOHTmKBwP7ptaa6FgnZm_JbNCu45klx8";

        String nonPiiId = (String) TokenUtil.extractFieldFromJwt(jwtToken, "non_pii_id");

        assertThat("Wrong value", nonPiiId, nullValue());
    }

    @Test
    public void extractFieldFromJwtForWrongString() {
        String jwtToken = "WRONG_STRING";

        String nonPiiId = (String) TokenUtil.extractFieldFromJwt(jwtToken, "non_pii_id");

        assertThat("Wrong value", nonPiiId, nullValue());
    }

    @Test
    public void extractTokenExpiration() {
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibm9uX3BpaV9pZCJdLCJ1c2VyX25hbWUiOiJib2IiLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTU4NTIyOTMyOCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjIxODEwZWUxLWI5MGQtNDQ4My1hYTE1LTJlNjZkOTc4MjhlZSIsImNsaWVudF9pZCI6ImVudGl0bGVtZW50cyIsIm5vbl9waWlfaWQiOiIyZjgtNGRhIn0.5Ef4wabYaGi5Ed56kD9bLip92BDx6-WeKNfmwh7P-wI";

        LocalDateTime result = TokenUtil.extractTokenExpiration(jwtToken);

        assertThat("Wrong value", result, is(LocalDateTime.parse("2020-03-26T13:28:48")));
    }

    @Test
    public void extractTokenExpirationWhenNoExpirationEncoded() {
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibm9uX3BpaV9pZCJdLCJ1c2VyX25hbWUiOiJib2IiLCJzY29wZSI6WyJyZWFkIl0sImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIyMTgxMGVlMS1iOTBkLTQ0ODMtYWExNS0yZTY2ZDk3ODI4ZWUiLCJjbGllbnRfaWQiOiJlbnRpdGxlbWVudHMiLCJub25fcGlpX2lkIjoiMmY4LTRkYSJ9.D3wXbMDuTEat2qd2JJaf9LHHKZPd66DuALAw6Jp9wNk";

        LocalDateTime result = TokenUtil.extractTokenExpiration(jwtToken);

        assertThat("Wrong value", result, nullValue());
    }

    @Test
    public void extractTokenExpirationForWrongString() {
        String jwtToken = "WRONG_STRING";

        LocalDateTime result = TokenUtil.extractTokenExpiration(jwtToken);

        assertThat("Wrong value", result, nullValue());
    }
}
