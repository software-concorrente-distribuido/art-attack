class X {
    private int lastIdUsed;

    public int getNextId(String t) {
        System.out.println("Chamado por: " + t + " - Last id used: " + lastIdUsed);
        return ++lastIdUsed;
    }
}

class XNaoSincronizado{
    public static void main(String[] args) {
        final X obj = new X();

        /*Simulando a condição de corrida por meio de Theads*/
        // Criando e iniciando a primeira thread
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    int nextId = obj.getNextId("A");
                    System.out.println("Thread A: " + nextId);
                }
            }
        });

        // Criando e iniciando a segunda thread
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    int nextId = obj.getNextId("B");
                    System.out.println("Thread B: " + nextId);
                }
            }
        });

        // Iniciando as duas threads
        threadA.start();
        threadB.start();
        
        /*As threads usam o método getLastId do objeto X compartilhado e, 
        como não há sincronização,é possível que elas obtenham e atualizem o
        método ao mesmo tempo, resultando em condição de corrida. Ou, ainda,
        os valores podem estar misturados, durante a exeução.
        */
    }
}