package com.stampbot.common;

import com.google.api.client.http.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

@Component
public class HttpUtils {

    @Autowired
    HttpRequestFactory httpRequestFactory;

    public static JSONObject parseResponse(HttpResponse response) throws IOException {
        Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        try {
            JSONObject jsonObj = new JSONObject(result);
            System.out.println(jsonObj.toString(2));
            return jsonObj;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new JSONObject();
    }

    public HttpResponse postResponseFromUrl(String url, HttpContent content) throws IOException {
        HttpRequest request = httpRequestFactory.buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setContentType("application/json");
        return request.execute();
    }

    public HttpResponse getResponseFromUrl(String url) throws IOException {
        HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(url));
        return request.execute();
    }
}
