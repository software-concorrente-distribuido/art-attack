package br.ufg;

public interface IMailBox {
    void storeMessage(String mensagem,String threadName) throws InterruptedException;
    String retriveMessage(String threadName) throws InterruptedException;
    Object getSinalizador();
}
