public class Algoritmo2 {
    private static boolean CA = false;
    private static boolean CB = false;

    public static void main(String[] args) {
        Thread processoA = new Thread(Algoritmo2::processoA);
        Thread processoB = new Thread(Algoritmo2::processoB);

        processoA.start();
        processoB.start();
    }

    public static void processoA() {
        do {
            while (CB); // Não faz nada enquanto CB for verdadeiro

            CA = true;
            // Região crítica A
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
            while (CA); // Não faz nada enquanto CA for verdadeiro

            CB = true;
            // Região crítica B
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