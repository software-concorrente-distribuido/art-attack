package br.ufg;

public class Consumidor extends Thread{
    IMailBox mailBox;

    Consumidor(String nome, IMailBox mailBox){
        super(nome);
        this.mailBox = mailBox;
    }

    @Override
    public void run() {
        try{
            String item;
                    synchronized (mailBox.getSinalizador()){
                        item = mailBox.retriveMessage(this.getName());
                    }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    void consumirItem(String item) throws InterruptedException {

        System.out.println("Consumida a msg-> " + item);

    }

}