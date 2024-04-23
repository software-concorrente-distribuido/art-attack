
import java.util.Queue;
import java.util.LinkedList;

/*
    Este código implementa o problema do produtor-consumidor em Java. O cenário
    simula múltiplas threads de download (produtores) baixando arquivos e
    colocando-os em um buffer compartilhado. Uma única thread de escrita em disco
    (consumidora) lê o buffer e escreve o conteúdo dos arquivos no disco. 
    A sincronização é feita usando monitoramento e notificações para garantir que
    as threads esperem quando o buffer estiver cheio ou vazio, evitando
    condições de corrida.
*/

public class ProdutorConsumidor {
    public static Queue<String> buffer = new LinkedList<>();
    public static final int BUFFER_SIZE = 5; //tamanho máximo do buffer.
    public static final int NUM_DOWNLOAD_THREADS = 3; //O número de threads de download (produtores)
    public static final int NUM_FILES_PER_THREAD = 3; //O número de arquivos que cada thread de download deve baixar.
    public static final Object lockObject = new Object(); //Um objeto de sincronização usado para garantir a exclusão mútua ao acessar o buffer.
    public static volatile boolean downloadComplete = false;

    //Inicia várias threads de download e uma thread de escrita em disco.
    public static void main(String[] args) {
        Thread[] downloadThreads = new Thread[NUM_DOWNLOAD_THREADS];
        Thread writerThread = new Thread(ProdutorConsumidor::writer);

        for (int i = 0; i < NUM_DOWNLOAD_THREADS; i++) {
            final int threadId = i;
            downloadThreads[i] = new Thread(() -> download(threadId));
            downloadThreads[i].start();
        }


        writerThread.start();

        try {
            for (Thread thread : downloadThreads) {
                thread.join();
            }
            downloadComplete = true;
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Cada thread de download baixa um número específico de arquivos.
    //Para cada arquivo baixado, a thread de download gera um conteúdo aleatório e o coloca no buffer compartilhado.
    //A sincronização é realizada usando um bloco synchronized com o objeto lockObject.
    //Se o buffer estiver cheio, a thread de download espera até que haja espaço disponível no buffer (lockObject.wait()).
    //Após colocar um arquivo no buffer, a thread de download notifica qualquer thread de escrita em espera (lockObject.notifyAll()).
    public static void download(int threadId) {
        for (int i = 0; i < NUM_FILES_PER_THREAD; i++) {
            String fileContent = "Arquivo " + threadId + "-" + i;
            synchronized (lockObject) {
                while (buffer.size() >= BUFFER_SIZE) {
                    try {
                        lockObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer.offer(fileContent);
                System.out.println("Thread " + threadId + " baixou: " + fileContent);
                lockObject.notifyAll();
            }
        }
    }

    //Se o buffer estiver vazio, a thread de escrita espera até que haja itens disponíveis para serem consumidos (lockObject.wait()).
    //Após consumir um arquivo do buffer, a thread de escrita notifica qualquer thread de download em espera (lockObject.notifyAll()).
    public static void writer() {
        while (!downloadComplete || !buffer.isEmpty()) {
            synchronized (lockObject) {
                while (buffer.isEmpty()) {
                    try {
                        if (downloadComplete) break;
                        lockObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!buffer.isEmpty()) {
                    String fileContent = buffer.poll();
                    System.out.println("Escrita em disco: " + fileContent);
                    lockObject.notifyAll();
                }
            }
        }
    }
}
