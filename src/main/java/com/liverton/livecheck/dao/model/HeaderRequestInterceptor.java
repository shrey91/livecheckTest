package com.liverton.livecheck.dao.model;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * Created by sshah on 23/08/2016.
 */
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

    private String authorizationKey;

    public HeaderRequestInterceptor(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest wrapper = new HttpRequestWrapper(request);
        wrapper.getHeaders().set("Authorization", "bearer "+ authorizationKey);
        wrapper.getHeaders().set("X-Version","1");
        return execution.execute(wrapper, body);
    }
}

