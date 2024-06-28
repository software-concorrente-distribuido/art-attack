package com.servidorftp;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int PORTA = 21;

    public static void main(String[] args) {
        new Servidor().iniciarServidor(); // Inicia o servidor
    }

    public void iniciarServidor() {

        try {
            // criando um servidor socket para escutar na porta definida
            ServerSocket servidorSocket = new ServerSocket(PORTA);

            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Servidor FTP iniciado em " + ip + ":" + PORTA);

            while (true) {
                // aceita as novas conexões de clientes
                Socket clienteSocket = servidorSocket.accept();

                // cria uma nova thread trabalhadora para cada cliente
                new Thread(new Trabalhador(clienteSocket)).start();

            }

        } catch (IOException e) {
            System.err.println("Não foi possível iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}