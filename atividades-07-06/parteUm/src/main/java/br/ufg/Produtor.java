package br.ufg;

public class Produtor extends Thread{

    IMailBox mailBox;

    Produtor(String nome, IMailBox mailBox){
        super(nome);
        this.mailBox = mailBox;
    }


    @Override
    public void run() {
        try{
            String item;

                item= produzirItem();

                synchronized (mailBox.getSinalizador()){
                    mailBox.storeMessage(item,this.getName());
                }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    String produzirItem() {
        int i = (int) Math.round(Math.random()*10);
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.append(this.getName()).toString();
    }
}
