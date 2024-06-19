package com.example.bufferserver;

import java.io.IOException;

public class ServerApplication {
    public static void main(String[] args) {
        int port = 8080;
        int bufferCapacity = 10;
        BufferServer server = new BufferServer(port, bufferCapacity);
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Falha ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
