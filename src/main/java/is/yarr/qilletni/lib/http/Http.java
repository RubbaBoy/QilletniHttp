package is.yarr.qilletni.lib.http;

import is.yarr.qilletni.api.lang.types.EntityType;
import is.yarr.qilletni.api.lang.types.IntType;
import is.yarr.qilletni.api.lang.types.JavaType;
import is.yarr.qilletni.api.lang.types.QilletniType;
import is.yarr.qilletni.api.lang.types.StringType;
import is.yarr.qilletni.api.lang.types.entity.EntityInitializer;
import is.yarr.qilletni.api.lib.annotations.NativeOn;
import is.yarr.qilletni.api.lib.annotations.SkipReturnTypeAdapter;
import is.yarr.qilletni.lib.http.exceptions.IllegalHeaderTypeException;
import is.yarr.qilletni.lib.http.exceptions.UnableToSendHttpRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Http {

    private final EntityInitializer entityInitializer;

    public Http(EntityInitializer entityInitializer) {
        this.entityInitializer = entityInitializer;
    }

    @SkipReturnTypeAdapter
    public static Object _createHttpClient() {
        return HttpClient.newHttpClient();
    }
    
    private static Map<String, String> ensureStringMap(Map<Object, Object> map) {
        var stringMap = new HashMap<String, String>();
        map.forEach((key, value) -> {
            var newKey = "";
            var newValue = "";
            
            if (key instanceof StringType keyString) {
                newKey = keyString.getValue();
            } else if (key instanceof String string) {
                newKey = string;
            } else {
                throw new IllegalHeaderTypeException("Header keys must be of type String or StringType");
            }
            
            if (value instanceof StringType keyString) {
                newValue = keyString.getValue();
            } else if (value instanceof String string) {
                newValue = string;
            } else {
                throw new IllegalHeaderTypeException("Header keys must be of type String or StringType");
            }
            
            stringMap.put(newKey, newValue);
        });
        
        return stringMap;
    }
    
    private static HttpRequest createRequest(EntityType entity) {
        var method = (StringType) entity.getEntityScope().lookup("method").getValue();
        var url = (StringType) entity.getEntityScope().lookup("url").getValue();
        var body = (StringType) entity.getEntityScope().lookup("body").getValue();
        var timeout = (IntType) entity.getEntityScope().lookup("timeout").getValue();
        var maxRedirects = (IntType) entity.getEntityScope().lookup("maxRedirects").getValue(); // TODO: Implement maxRedirects
        
        var headerEntity = (EntityType) entity.getEntityScope().lookup("headers").getValue();
        // If it's not a String, String eat shit
        Map<Object, Object> headerMap = headerEntity.getEntityScope().<JavaType>lookup("_map").getValue().getReference(HashMap.class);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url.getValue()))
                .timeout(Duration.ofSeconds(timeout.getValue()));

        ensureStringMap(headerMap).forEach(requestBuilder::header);

        return requestBuilder.method(method.getValue(), HttpRequest.BodyPublishers.ofString(body.getValue())).build();
    }
    
    private EntityType createHttpResponse(HttpResponse<?> httpResponse) {
        var headers = new HashMap<String, Object>();
        httpResponse.headers().map().forEach((key, value) -> {
            if (value.size() == 1) {
                headers.put(key, value.getFirst());
            } else {
                headers.put(key, value);
            }
        });
        
        return entityInitializer.initializeEntity("HttpResponse", httpResponse.statusCode(), headers, httpResponse.body());
    }
    
    @NativeOn("HttpClient")
    public EntityType send(EntityType entity, QilletniType request) {
        var javaType = entity.getEntityScope().<JavaType>lookup("_internalClient").getValue();
        var httpClient = javaType.getReference(HttpClient.class);
        
        if (!(request instanceof EntityType entityType)) {
            throw new UnableToSendHttpRequest("Must supply an Entity in send()");
        }

        try {
            var created = createRequest(entityType);

            return createHttpResponse(httpClient.send(created, HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException e) {
            throw new UnableToSendHttpRequest(e);
        }
    }
}
