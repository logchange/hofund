package dev.logchange.hofund.connection;

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


    RequestMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
