package com.stampbot.workflow.utils;

import com.google.api.client.http.*;
import org.json.JSONArray;
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

    public static JSONArray parseResponses(HttpResponse response) throws IOException {
        Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        try {
            JSONArray jsonArray = new JSONArray(result);
            System.out.println(jsonArray.toString(2));
            return jsonArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new JSONArray();
    }

    public HttpResponse postResponseFromUrl(String url, HttpContent content) throws IOException {
        HttpRequest request = httpRequestFactory.buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setContentType("application/json");
        return request.execute();
    }

    public HttpResponse putResponseFromUrl(String url, HttpContent content) throws IOException {
        HttpRequest request = httpRequestFactory.buildPutRequest(new GenericUrl(url), content);
        request.getHeaders().setContentType("application/json");
        return request.execute();
    }

    public HttpResponse getResponseFromUrl(String url) throws IOException {
        HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(url));
        return request.execute();
    }
}
