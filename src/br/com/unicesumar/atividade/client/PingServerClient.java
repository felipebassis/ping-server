package br.com.unicesumar.atividade.client;

import br.com.unicesumar.atividade.server.PingServer;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class PingServerClient {

    public static void main(String[] args) {
        PingServerClient client = new PingServerClient();
        client.run();
    }

    private void run() {
        PrintStream clientPrintStream = System.out;
        try (Socket connection = new Socket(PingServer.HOSTNAME, PingServer.PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            List<Long> requestTimes = new ArrayList<>();
            long timeBeforeAllRequests = System.nanoTime();
            for (int count = 0; count < 1000; count++) {
                out.write("ping\n");
                out.flush();
                long timeBeforeRequest = System.nanoTime();
                String message = in.readLine();
                long timeAfterRequest = System.nanoTime();
                clientPrintStream.println(message);
                long requestTime = timeAfterRequest - timeBeforeRequest;
                requestTimes.add(requestTime);
            }
            out.write("end\n");
            out.flush();
            clientPrintStream.println(in.readLine());
            long timeAfterAllRequests = System.nanoTime();
            clientPrintStream.print("Tempo total: " + (Duration.of(timeAfterAllRequests - timeBeforeAllRequests, ChronoUnit.NANOS)).toMillis() + "ms\n" +
                    "Média de tempo: " + requestTimes.stream()
                    .mapToLong(value -> value)
                    .average()
                    .getAsDouble() + "ns\n" +
                    "Requisição mais rápida: " + requestTimes.stream()
                    .min(Comparator.comparing(Function.identity()))
                    .get() + "ns");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
