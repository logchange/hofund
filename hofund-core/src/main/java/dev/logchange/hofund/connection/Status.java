package dev.logchange.hofund.connection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Status {

    private static final double UP_VALUE = 1.0;
    public static final Status UP = new Status(UP_VALUE);
    private static final double DOWN_DOWN = 0.0;
    public static final Status DOWN = new Status(DOWN_DOWN);

    private final double value;

}
