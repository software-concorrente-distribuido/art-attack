package com.example.bufferserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class BufferServer {
    private int port;
    private BufferManager bufferManager;

    public BufferServer(int port, int bufferCapacity) {
        this.port = port;
        this.bufferManager = new BufferManager(bufferCapacity);
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server iniciando na porta " + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket, bufferManager).start();
        }
    }
}

