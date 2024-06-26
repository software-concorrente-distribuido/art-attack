package com.example.bufferserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class BufferManager {
    private int[] buffer;
    private int tamanho;
    private int insertPos = 0;
    private int removePos = 0;
    private Semaphore full, empty, mutex;

    public BufferManager(int tamanho) {
        this.tamanho = tamanho;
        this.buffer = new int[tamanho];
        this.full = new Semaphore(0);
        this.empty = new Semaphore(tamanho);
        this.mutex = new Semaphore(1);
    }

    public void inserir(int item, DataOutputStream out) throws InterruptedException, IOException {
        if (!empty.tryAcquire()) {
            System.out.println(Thread.currentThread().getName() + " falhou ao produzir " + item + ". BUFFER CHEIO!");
            out.writeUTF("Rejected");
        } else {
            mutex.acquire();
            buffer[insertPos] = item;
            insertPos = (insertPos + 1) % tamanho;
            System.out.println(Thread.currentThread().getName() + " produziu: " + item + ", BUFFER: " + Arrays.toString(buffer));
            mutex.release();
            full.release();
            out.writeUTF("Aceito");
        }
        out.flush();
    }



    public void remove(DataOutputStream out) throws InterruptedException, IOException {
        if (!full.tryAcquire()) {
            System.out.println(Thread.currentThread().getName() + " tentou consumir mas o BUFFER está VAZIO!");
            out.writeUTF("Buffer empty");
        } else {
            mutex.acquire();
            int item = buffer[removePos];
            buffer[removePos] = 0;
            removePos = (removePos + 1) % tamanho;
            System.out.println(Thread.currentThread().getName() + " consumiu: " + item + ", BUFFER: " + Arrays.toString(buffer));
            mutex.release();
            empty.release();
            out.writeUTF("Item: " + item);
        }
        out.flush();
    }
}
