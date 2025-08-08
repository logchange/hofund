package dev.logchange.hofund.connection;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import static org.slf4j.LoggerFactory.getLogger;

public class HofundConnectionResult {

    private static final Logger log = getLogger(HofundConnectionResult.class);
    public static final String UNKNOWN = "UNKNOWN";
    public static final String NOT_APPLICABLE = "N/A";

    private final Status status;
    private final Version version;


    private HofundConnectionResult(Status status, String version) {
        this.status = status;
        this.version = Version.of(version);
    }

    public static HofundConnectionResult db(Status status) {
        return new HofundConnectionResult(status, NOT_APPLICABLE);
    }

    public static HofundConnectionResult http(Status status, String version) {
        return new HofundConnectionResult(status, version);
    }

    public static HofundConnectionResult http(Status status, HttpURLConnection openConnection) {
        String body = parseResponseBody(openConnection);
        String version = extractVersionFromResponse(body);
        log.debug("Extracted version: {}", version);
        return HofundConnectionResult.http(status, version);
    }

    private static String parseResponseBody(HttpURLConnection urlConn) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            String responseBody = response.toString();
            log.debug("Response body: {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            log.warn("Error reading response from: {} msg: {}", urlConn.getURL(), e.getMessage());
            log.debug("Exception: ", e);
            return null;
        }
    }

    private static String extractVersionFromResponse(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            return UNKNOWN;
        }

        int applicationIndex = responseBody.indexOf("\"application\"");
        if (applicationIndex == -1) {
            return UNKNOWN;
        }

        String versionKey = "\"version\":\"";
        int versionIndex = responseBody.indexOf(versionKey, applicationIndex);
        if (versionIndex == -1) {
            return UNKNOWN;
        }

        int versionValueIndex = responseBody.indexOf(versionKey, applicationIndex) + versionKey.length();
        int closeQuoteIndex = responseBody.indexOf("\"", versionValueIndex);
        if (closeQuoteIndex == -1) {
            return UNKNOWN;
        }

        String version = responseBody.substring(versionValueIndex, closeQuoteIndex);
        if (version.isEmpty()) {
            return UNKNOWN;
        }

        return version;
    }

    public Status getStatus() {
        return status;
    }

    public String getVersion() {
        return version.toString();
    }
}
