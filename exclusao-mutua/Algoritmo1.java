public class Algoritmo1 {
    private static char vez = 'A'; // 'A' representa a vez do processo A, 'B' representa a vez do processo B

    public static void main(String[] args) {
        Thread processoA = new Thread(Algoritmo1::processoA);
        Thread processoB = new Thread(Algoritmo1::processoB);

        processoA.start();
        processoB.start();
    }

    public static void processoA() {
        while (true) {
            while (vez != 'A'); // Aguarda até que seja a vez do processo A

            // Região crítica de A
            System.out.println("Região crítica de A");
            // Processamento de A
            System.out.println("Processamento de A");

            vez = 'B'; // Passa a vez para B
            
            // Aguarda um tempo para simular outro processamento
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void processoB() {
        while (true) {
            while (vez != 'B'); // Aguarda até que seja a vez do processo B

            // Região crítica de B
            System.out.println("Região crítica de B");
            // Processamento de B
            System.out.println("Processamento de B");

            vez = 'A'; // Passa a vez para A
            
            // Aguarda um tempo para simular outro processamento
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}