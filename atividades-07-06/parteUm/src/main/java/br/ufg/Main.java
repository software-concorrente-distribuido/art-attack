package br.ufg;

import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        IMailBox mailBox = new MailBox();
        Thread[] threads = new Thread[12];

        // Cria 6 produtores e 6 consumidores
        for (int i = 0; i < 6; i++) {
            threads[i] = new Produtor("Produtor " + (i + 1), mailBox);
            threads[i + 6] = new Consumidor("Consumidor " + (i + 1), mailBox);
        }
        for (Thread thread : threads) {
            thread.start();
        }



    }


}