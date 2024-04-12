/* o método getNextId() é modificado para ser synchronized, garantindo
que apenas uma thread por vez possa executar esse método em uma instância
específica de X. 

Com essa modificação, o código garante que os
resultados serão consistentes e que as threads não entrarão em conflito ao
acessar o método getNextId().*/

class X {
    private int lastIdUsed;

    // Método synchronized para garantir acesso exclusivo
    public synchronized int getNextId(String t) {
        System.out.println("Chamado por: " + t + " - Last id used: " + lastIdUsed);
        return ++lastIdUsed;
    }
}


class XSincronizado {
    public static void main(String[] args) {
        final X obj = new X();

        /* Simulando a condição de corrida por meio de Threads */
        // Criando e iniciando a primeira thread
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    synchronized (obj){
                        int nextId = obj.getNextId("A");
                        System.out.println("Thread A: " + nextId);
                    }
                }
            }
        });

        // Criando e iniciando a segunda thread
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    synchronized (obj){
                        int nextId = obj.getNextId("B");
                        System.out.println("Thread B: " + nextId);
                    }
                }
            }
        });

        // Iniciando as duas threads
        threadA.start();
        threadB.start();
    }
    
}