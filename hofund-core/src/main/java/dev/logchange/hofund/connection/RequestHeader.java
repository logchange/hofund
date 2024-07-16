package dev.logchange.hofund.connection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestHeader {
    private final String name;
    private final String value;

    public static RequestHeader of(String name, String value) {
        return new RequestHeader(name, value);
    }
}
