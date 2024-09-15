import "json:json.ql"

/**
 * Represents a single HTTP request.
 */
entity HttpRequest {

    /**
     * The HTTP method to use for the request.
     */
    string method
    
    /**
     * The full URL of the request
     */
    string url
    
    /**
     * The headers to send with the request. Both key and values are strings.
     */
    Map headers
    
    /**
     * The body of the request.
     */
    string body
    
    /**
     * The timeout for the request in milliseconds.
     */
    int timeout = 5000
    
    /**
     * The maximum number of redirects to follow.
     */
    int maxRedirects = 5
    
    /**
     * Creates a new HTTP request. Recommended to use the static factory methods instead.
     */
    HttpRequest(method, url, headers, body)
    
    /**
     * Creates a new HTTP request with a specified method, to be sent with `HttpClient.send()`.
     *
     * @param[@type string] method    The HTTP method to use for the request
     * @param[@type string] url       The URL to send the request to
     * @param[@type std.Map] headers The headers to send with the request
     * @param body                    The body of the request. May either be a string or a Map
     * @returns[@type http.HttpRequest] The created request
     */
    static fun create(method, url, headers, body) {
        any createdBody = body
        if (body is Map) {
            createdBody = JsonConverter.createJsonConverter(true).toJson(body)
        }
        
        return new HttpRequest(method, url, headers, createdBody)
    }
    
    /**
     * Creates a new HTTP GET request to be sent with `HttpClient.send()`.
     *
     * @param[@type string] url       The URL to send the request to
     * @param[@type std.Map] headers The headers to send with the request
     * @param body                    The body of the request. May either be a string or a Map
     * @returns[@type http.HttpRequest] The created request
     */
    static fun createGet(url, headers, body) {
        return HttpRequest.create("GET", url, headers, body)
    }
    
    /**
     * Creates a new HTTP GET request to be sent with `HttpClient.send()`.
     *
     * @param[@type string] url       The URL to send the request to
     * @param[@type std.Map] headers The headers to send with the request
     * @param body                    The body of the request. May either be a string or a Map
     * @returns[@type http.HttpRequest] The created request
     */
    static fun createPost(url, headers, body) {
        return HttpRequest.create("POST", url, headers, body)
    }
}

/**
 * Represents a single HTTP response.
 */
entity HttpResponse {
    
    /**
     * The status code of the response
     */
    int statusCode
    
    /**
     * The headers of the response
     */
    Map headers
    
    /**
     * The body of the response
     */
    string body
    
    /**
     * Creates a new HTTP response with the given status code, headers, and body.
     */
    HttpResponse(statusCode, headers, body)
}

/**
 * An HTTP client that can send requests and receive responses.
 *
 * @returns[@type @java java.net.http.HttpClient] The created HttpClient 
 */
native fun _createHttpClient()

/**
 * Represents a stateful HTTP client that can send requests and receive responses.
 */
entity HttpClient {

    /**
     * The internal HTTP client object.
     * @type @java java.net.http.HttpClient
     */
    java _internalClient = _createHttpClient()

    /**
     * Sends an HTTP request and returns the response.
     *
     * @param[@type http.HttpRequest] request The request to send
     * @returns[@type http.HttpResponse] The response to the request
     */
    native fun send(request)
}
