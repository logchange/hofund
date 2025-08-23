package dev.logchange.hofund.connection;

import static dev.logchange.hofund.connection.HofundConnectionResult.NOT_APPLICABLE;
import static dev.logchange.hofund.connection.HofundConnectionResult.UNKNOWN;

class Version implements Comparable<Version> {

    private final String value;

    private Version(String value) {
        this.value = value;
    }

    static Version of(String value) {
        if (value == null || value.isEmpty()) {
            return new Version(NOT_APPLICABLE);
        }
        return new Version(value);
    }

    private boolean isUnknown() {
        return UNKNOWN.equals(value);
    }

    private boolean isNotApplicable() {
        return NOT_APPLICABLE.equals(value);
    }

    public boolean isUnspecified() {
        return isUnknown() || isNotApplicable();
    }

    @Override
    public int compareTo(Version other) {
        if (value.equals(other.value)) {
            return 0;
        }

        boolean isThisUnspecified = this.isUnspecified();
        boolean isOtherUnspecified = other.isUnspecified();
        if (isThisUnspecified && isOtherUnspecified) {
            return 0;
        }

        if (isThisUnspecified || isOtherUnspecified) {
            throw new IllegalArgumentException("Cannot compare regular versions with unspecified!");
        }

        String[] thisParts = value.split("\\.");
        String[] otherParts = other.value.split("\\.");

        int length = Math.max(thisParts.length, otherParts.length);
        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ? parseInt(thisParts[i]) : 0;
            int otherPart = i < otherParts.length ? parseInt(otherParts[i]) : 0;

            if (thisPart < otherPart) {
                return -1;
            }
            if (thisPart > otherPart) {
                return 1;
            }
        }

        return 0;
    }

    private int parseInt(String part) {
        StringBuilder numericPart = new StringBuilder();
        for (int i = 0; i < part.length(); i++) {
            if (Character.isDigit(part.charAt(i))) {
                numericPart.append(part.charAt(i));
            } else {
                break;
            }
        }
        if (numericPart.length() > 0) {
            return Integer.parseInt(numericPart.toString());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return value.equals(version.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
