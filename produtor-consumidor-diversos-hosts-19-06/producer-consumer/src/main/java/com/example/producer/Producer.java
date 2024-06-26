package com.example.producer;

import java.io.DataInputStream;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class Producer {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Random random;

    public Producer(String address, int port) throws IOException {
        socket = new Socket(address, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        random = new Random();
    }

    public void produce() throws IOException, InterruptedException {
        while (true) {
            int item = random.nextInt(100);
            out.writeUTF("produzir " + item);
            out.flush();
            // Espera pela resposta do servidor
            String response = in.readUTF();
            if (response.equals("Aceito")) {
                System.out.println("Produtor enviou: produzir " + item);
            } else {
                System.out.println("Falha ao produzir " + item + ". BUFFER CHEIO.");
                Thread.sleep(1000);
            }
            Thread.sleep(random.nextInt(3000));
        }
    }

    public static void main(String[] args) {
        String address = "localhost";
        int port = 8080;

        try {
            Producer producer = new Producer(address, port);
            producer.produce();
        } catch (IOException e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Producer interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }
}