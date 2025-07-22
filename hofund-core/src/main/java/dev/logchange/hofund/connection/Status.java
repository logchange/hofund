package dev.logchange.hofund.connection;

public enum Status {

    UP("UP", 1.0),
    DOWN("DOWN", 0.0),
    INACTIVE("INACTIVE", -1.0);

    private final String name;
    private final double value;

    Status(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
