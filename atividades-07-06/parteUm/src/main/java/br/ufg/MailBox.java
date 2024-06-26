package br.ufg;

public class MailBox implements IMailBox{
    private final String[] mensagens;

    final Object sinalizador;

    public MailBox(){
        mensagens = new String[1];
        sinalizador = new Object();
    }

    public  void storeMessage(String mensagem, String threadName) throws InterruptedException {
        if(mensagens[0] ==null) {
            mensagens[0] = mensagem;
            sinalizador.notifyAll();
            System.out.println("Guardada: "+mensagem);
        }else{
            sinalizador.wait();
            System.out.println(threadName+" -> Após espera, vou tentar guardar a mensagem");
            storeMessage(mensagem,threadName);
        }
    }

    public  String retriveMessage(String threadName) throws InterruptedException {
        if(mensagens[0] !=null) {
            String msg = mensagens[0];
            mensagens[0] = null;
            System.out.println("Retirada: "+msg);
            sinalizador.notifyAll();
            return msg;
        }else{

            sinalizador.wait();
            System.out.println(threadName+ " -> Após espera, vou tentar retornar a mensagem");
            return this.retriveMessage(threadName);
        }

    }

    @Override
    public Object getSinalizador() {
        return this.sinalizador;
    }

}
