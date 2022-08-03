package jensha.httpclient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {


    @Test
    void get() {
        HttpResponse httpResponse = HttpClient.get(null, "https://example.com");
        assertEquals(httpResponse.statusCode, 200);
        assertTrue(httpResponse.body.contains("<title>Example Domain</title>"));
    }
}
