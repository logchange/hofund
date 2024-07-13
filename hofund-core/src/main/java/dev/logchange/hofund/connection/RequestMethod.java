package dev.logchange.hofund.connection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestMethod {

    GET("GET"),
    POST("POST"),
    PATCH("PATH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE");

    private final String name;
}
