package jensha.httpclient

import jensha.utils.PScript
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients

/**
 * Client is static because CPS exceptions. Don't even try fancy OOP. I tried...
 * If you need a special client for some API just duplicate this class and reimplement it according to your needs.
 */
class HttpClient implements Serializable {

    private static CloseableHttpClient httpClient = null

    private HttpClient() {}

    private static void init() {
        if (httpClient != null) {
            return
        }

        httpClient = HttpClients.custom()
                .setSSLSocketFactory(HttpClientUtils.selfSignedCertsSslSocketFactory())
                .build()
    }

    static HttpResponse get(PScript pscript, String url) {
        init()

        try {
            HttpGet httpGet = new HttpGet(url)
            CloseableHttpResponse response = httpClient.execute(httpGet)
            return HttpClientUtils.toHttpResponse(response)
        } catch (Exception e) {
            pscript.exitWithError("Failed to GET ${url}", e)
            throw e
        }
    }


}
