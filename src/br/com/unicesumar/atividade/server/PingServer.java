package br.com.unicesumar.atividade.server;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class PingServer {

    public static final int PORT = 8080;

    public static final String HOSTNAME = "localhost";

    public static final PrintStream SYSTEM_WRITER = System.out;

    public static void main(String[] args) {
        PingServer pingServer = new PingServer();
        pingServer.runServer();
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                PingServerHandler pingServerHandler = new PingServerHandler(serverSocket.accept());
                SYSTEM_WRITER.println("Request received");
                pingServerHandler.start();
                SYSTEM_WRITER.println("Request handled");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }
}
