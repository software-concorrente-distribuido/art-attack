package com.example.bufferserver;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferManager bufferManager;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket, BufferManager bufferManager) {
        this.clientSocket = socket;
        this.bufferManager = bufferManager;
        try {
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                String command = in.readUTF();
                if (command.startsWith("produzir")) {
                    int item = Integer.parseInt(command.split(" ")[1]);
                    bufferManager.inserir(item, out);
                } else if (command.equals("consumir")) {
                    bufferManager.remove(out);
                }
            }
        } catch (IOException e) {
            if ("Connection reset".equals(e.getMessage())) {
                System.out.println("Conexão reiniciada pelo cliente.");
            } else {
                System.out.println("IO exception: " + e.getMessage());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
