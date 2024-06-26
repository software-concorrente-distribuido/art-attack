package com.example.producer;

import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Consumer {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public Consumer(String address, int port) throws IOException {
        socket = new Socket(address, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
    }

    public void consume() throws IOException {
        while (true) {
            out.writeUTF("consumir");
            out.flush();

            String response = in.readUTF();
            if (response.startsWith("Item:")) {
                System.out.println("Consumidor recebeu > " + response);
            } else {
                System.out.println("Consumidor tentou consumir, mas o BUFFER está VAZIO.");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.err.println("Consumer interrupted: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String address = "localhost";
        int port = 8080;

        try {
            Consumer consumer = new Consumer(address, port);
            consumer.consume();
        } catch (IOException e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
