package jensha.httpclient

import org.apache.http.Header

class HttpHeader implements Serializable {

    public final String name
    public final String value

    HttpHeader(String name, String value) {
        this.name = name
        this.value = value
    }

    HttpHeader(Header header) {
        this.name = header.getName()
        this.value = header.getValue()
    }

}
