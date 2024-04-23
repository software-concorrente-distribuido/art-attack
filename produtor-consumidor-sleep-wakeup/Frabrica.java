import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Frabrica {
    final int N = 5;
    Integer count = 0;
    Integer[] buffer = new Integer[N];
    final Object vez = new Object();

    void iniciar(){

        var produtor = new Produtor("produtor");

        var consumidor = new Consumidor("consumidor");

        produtor.start();

        consumidor.start();

    }

    private class Produtor extends Thread{

        Produtor(String nome){
            super(nome);
        }


        @Override
        public void run() {
            try{
                int item;
                while(true){

                    System.out.println("[PRODUTOR]  LOOP START");

                    item = produzirItem();

                        if(count == N){
                            synchronized (vez){
                                System.out.println("[PRODUTOR]  O meu buffer enxeu, vou parar de produzir até que me notifiquem");
                                vez.wait();
                                System.out.println("[PRODUTOR]  Consumiram lá, vou voltar a produzir!");
                            }
                        }
                        insertItem(item);
                        count++;
                        if(count==1){
                            synchronized (vez){
                                System.out.println("[PRODUTOR]  Já tem coisa disponível, vou notificar o consumidor!");
                                vez.notify();
                            }
                        }

                }
            }catch (Exception e){

            }

        }
    }
    private class Consumidor extends Thread{

        Consumidor(String nome){
            super(nome);
        }

        @Override
        public void run() {
            try{
                Integer item;
                while(true){
                    System.out.println("[CONSUMIDOR]  LOOP START");

                        if(count == 0){
                            synchronized (vez){
                                System.out.println("[CONSUMIDOR]  Acabaram os itens, esperando me notificarem para consumir...");
                                vez.wait();
                                System.out.println("[CONSUMIDOR]  Me acordaram! Vou comer!");
                            }
                        }

                        item = removeItem();

                    System.out.println(count);
                        count--;

                        if(count == N-1){
                            synchronized (vez){
                                System.out.println("[CONSUMIDOR]  Ja pode começar a fazer mais itens! Vou lhe notificar");
                                vez.notify();
                            }
                        }

                        consumirItem(item);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private void consumirItem(Integer i) throws InterruptedException {

        System.out.println("[CONSUMIDOR]  Consumindo item "+i);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(buffer[0] +" "+ buffer[1]+ " "+ buffer[2] + " "+ buffer[3]+ " "+ buffer[4]);
        stringBuilder.append("]");
        System.out.println(stringBuilder);


        System.out.println("[CONSUMIDOR]  Item consumido!");

    }

    private Integer removeItem() {
        Integer i;


        System.out.println("[CONSUMIDOR]  Item removido: "+buffer[count-1]);
         i = buffer[count-1];
        buffer[count-1] = null;

        return i;
    }


    private void insertItem(int item) {
        System.out.println("[PRODUTOR]  Item inserido: "+ item);

        buffer[count] = item;

    }

    private int produzirItem() {
        Integer i = (int) Math.round(Math.random()*10);
        System.out.println("[PRODUTOR]  Item produzido: "+ i);


        return i;
    }

}
