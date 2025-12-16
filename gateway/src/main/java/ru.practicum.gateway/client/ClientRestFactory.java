package ru.practicum.gateway.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public final class ClientRestFactory {

    private ClientRestFactory() {
    }

    public static RestTemplate build(String baseUrl, RestTemplateBuilder builder) {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                .requestFactory(() -> requestFactory)
                .build();
    }
}
