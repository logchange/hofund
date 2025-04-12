package dev.logchange.hofund.connection.spring.http;

import org.codehaus.plexus.component.annotations.Component;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapException;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;

import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CaptureTraffic {

    void main() throws PcapException {
        List<PcapIf> devices = Pcap.findAllDevs();
        try (Pcap pcap = Pcap.create(devices.get(0))) {
            pcap.activate();
            pcap.loop(1, (String msg, PcapHeader header, byte[] packet) -> {
                System.out.println(msg);
            }, "Capture Example");
        }

        HttpClient.newBuilder().build();
    }

    public static void main(String[] args) throws PcapException {
        System.out.println("Przed inicjalizacji");
        new CaptureTraffic().main();
        System.out.println("Po inicjalizacji");

        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://www.google.com"))
                    .GET() // Optional, as GET is the default method
                    .build();

            // Send request and receive HttpResponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: ");
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
