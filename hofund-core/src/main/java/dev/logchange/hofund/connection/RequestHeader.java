package dev.logchange.hofund.connection;

public class RequestHeader {
    private final String name;
    private final String value;

    private RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static RequestHeader of(String name, String value) {
        return new RequestHeader(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
