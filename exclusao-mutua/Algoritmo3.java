/*Cada processo primeiro define sua variável correspondente (CA ou CB)
como verdadeira antes de entrar na região crítica.
Determina a variável correspondente como falsa logo após sair da região
crítica*/

public class Algoritmo3 {
    private static boolean CA = false;
    private static boolean CB = false;

    public static void main(String[] args) {
        Thread processoA = new Thread(Algoritmo3::processoA);
        Thread processoB = new Thread(Algoritmo3::processoB);

        processoA.start();
        processoB.start();
    }

    public static void processoA() {
        do {
            CA = true;
            
            // Espera ate que CB seja false
            while (CB); 
            
            // Entra na regiao critica A
            System.out.println("Regiao critica A");
            
            CA = false;
            
            // Processamento A
            System.out.println("Processamento A");

            // Aguarda um tempo para simular outro processamento
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (true);
    }

    public static void processoB() {
        do {
            CB = true;
            
            // Espera ate que CB seja false
            while (CA);
            
            // Entra na regiao critica B
            System.out.println("Regiao critica B");
            
            CB = false;
            
            // Processamento B
            System.out.println("Processamento B");

            // Aguarda um tempo para simular outro processamento
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } while (true);
    }
}
