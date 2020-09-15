package br.com.unicesumar.atividade.server;

import java.io.*;
import java.net.Socket;

public class PingServerHandler extends Thread {
    private final Socket socket;

    public PingServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Writer out = new PrintWriter(this.socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while (true) {
                String message = in.readLine();
                if (message.equals("end")) {
                    out.write("closing connection...\n");
                    out.flush();
                    this.socket.close();
                    break;
                }
                if (message.equals("ping")) {
                    out.write("pong\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
