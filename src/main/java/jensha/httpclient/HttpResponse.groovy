package jensha.httpclient

class HttpResponse implements Serializable {

    public final List<HttpHeader> httpHeaders
    public final int statusCode
    public final String body

    HttpResponse(List<HttpHeader> httpHeaders, int statusCode, String body) {
        this.httpHeaders = httpHeaders
        this.statusCode = statusCode
        this.body = body
    }

    @Override
    String toString() {

        StringBuilder sb = new StringBuilder()
        sb.append("STATUS CODE").append(": ").append(this.statusCode).append("\n")
        for (HttpHeader header : this.httpHeaders) {
            sb.append(header.name).append(": ").append(header.value).append("\n")
        }
        sb.append("\n")
        sb.append(this.body)

        return sb.toString()
    }
}
