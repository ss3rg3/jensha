package jensha.httpclient

import org.apache.http.Header
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.ssl.SSLContextBuilder
import org.apache.http.util.EntityUtils

class HttpClientUtils implements Serializable {

    static HttpResponse toHttpResponse(CloseableHttpResponse response) {
        try {
            int statusCode = response.getStatusLine().statusCode
            String body = EntityUtils.toString(response.getEntity())

            List<HttpHeader> headers = new ArrayList<>()
            for (Header header : response.allHeaders) {
                headers.add(new HttpHeader(header))
            }

            return new HttpResponse(headers, statusCode, body)
        } finally {
            response.close()
        }
    }

    static SSLConnectionSocketFactory selfSignedCertsSslSocketFactory() {
        SSLContextBuilder builder = new SSLContextBuilder()
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy())

        return new SSLConnectionSocketFactory(
                builder.build(), NoopHostnameVerifier.INSTANCE)
    }

}
