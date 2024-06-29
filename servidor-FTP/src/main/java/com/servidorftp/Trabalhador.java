package com.servidorftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Trabalhador implements Runnable {

    private Socket socketControle;
    private Socket socketDados;
    private ServerSocket serverSocketDados;
    private BufferedReader entradaControle;
    private PrintWriter saidaControle;

    private GerenciadorUsuarios gerenciadorUsuarios;
    private File diretorioAtual;
    private String separadorArquivo = System.getProperty("file.separator");

    private String usuarioAtual = null;
    private boolean isLogged = false;

    public Trabalhador(Socket socketControle) {
        this.socketControle = socketControle;
        this.gerenciadorUsuarios = new GerenciadorUsuarios();
        this.diretorioAtual = new File("arquivos");
    }

    @Override
    public void run() {

        // coletando informações para log do servidor
        String threadId = "Thread " + Thread.currentThread().getId();
        String clienteInfo = socketControle.getInetAddress().getHostAddress() + ":" + socketControle.getPort();
        System.out.println(threadId + " - Conexão iniciada com " + clienteInfo);

        try {
            entradaControle = new BufferedReader(new InputStreamReader(socketControle.getInputStream())); // leitor de comandos do cliente
            saidaControle = new PrintWriter(socketControle.getOutputStream(), true); // escritor de respostas para o cliente

            // envia mensagem de boas-vindas ao cliente
            enviarResposta("220 - Bem-vindo ao Servidor FTP. Digite HELP para visualizar os comandos disponíveis.");
        } catch (IOException e) {
            System.out.println("Erro ao configurar streams de entrada/saída: " + e.getMessage());
        }

        try{
            // lê e trata os comandos do clietne
            String comando;

            while ((comando = entradaControle.readLine()) != null) {
                System.out.println(threadId + " - Comando recebido: " + comando);
                tratarComando(comando);
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler comando do cliente: " + e.getMessage());
        } finally {
            fecharConexoes();
            System.out.println(threadId + " - Conexão encerrada com " + clienteInfo);
        }
    }

    private void tratarComando(String comando) {

        //divide o comando e os argumentos
        //exemplo: se o comando for USER usuario, o resultado será ["USER", "joao"]
        String[] partes = comando.split(" ", 2);
        String cmd = partes[0].toUpperCase();
        String arg = partes.length > 1 ? partes[1] : null; // verifica e há argumentos no comando

        try {
            switch (cmd) {
                case "USER":
                    tratarUsuario(arg);
                    break;
                case "PASS":
                    tratarSenha(arg);
                    break;
                case "LIST":
                    tratarLista();
                    break;
                case "RETR":
                    tratarRetorno(arg);
                    break;
                case "STOR":
                    tratarArmazenar(arg);
                    break;
                case "QUIT":
                    tratarSair();
                    break;
                case "CWD":
                    tratarMudarDiretorio(arg);
                    break;
                case "PWD":
                    tratarImprimirDiretorio();
                    break;
                case "PASV":
                    tratarPASV();
                    break;
                case "HELP":
                    tratarAjuda();
                    break;
                default:
                    enviarResposta("502 - Comando não implementado"); // Responde se o comando não é implementado
            }
        }catch (IOException e) {
            System.out.println("Erro ao tratar o comando " + cmd + ": " + e.getMessage());
        }
    }

    private void tratarUsuario(String usuario) {

        if (gerenciadorUsuarios.validarUsuario(usuario)) {
            usuarioAtual = usuario;
            enviarResposta("331 - Nome de usuário OK, precisa de senha");
        } else {
            enviarResposta("530 - Nome de usuário inválido");
        }
    }

    private void tratarSenha(String senha) {

        if (usuarioAtual != null && gerenciadorUsuarios.validarSenha(usuarioAtual, senha)) {
            isLogged = true;
            enviarResposta("230 - Usuário logado");
        } else {
            enviarResposta("530 - Senha inválida");
        }
    }

    // metodo para retornar a lista de arquivos do diretório
    private void tratarLista() {

        // Verifica se o usuário está logado
        if (!isLogged) {
            enviarResposta("530 - Não logado");
            return;
        }

        if(serverSocketDados == null || serverSocketDados.isClosed()){
            enviarResposta("421 - Serviço não disponível");
            return;
        }

        File[] arquivos = diretorioAtual.listFiles();

        if (arquivos == null) {
            enviarResposta("550 - Arquivo é null Falha ao listar diretório"); // Responde com erro se não puder listar o diretório
            return;
        }
        enviarResposta("150 - Abrindo conexão de dados em modo ASCII para lista de arquivos");
        System.out.println("Ok");
        try {
            socketDados = serverSocketDados.accept();
            PrintWriter dadosSaida = new PrintWriter(socketDados.getOutputStream(), true);

            for (File arquivo : arquivos) {
                dadosSaida.println(arquivo.getName());
            }

            dadosSaida.flush();
            dadosSaida.close();
            socketDados.close();

            enviarResposta("226 - Transferência concluída");
        } catch (IOException e) {
            System.out.println("Erro ao listar arquivos: " + e.getMessage());
            enviarResposta("550 - Falha ao listar diretório");
        }

    }



    // metodo para retornar o arquivo solicitado para o cliente
    private void tratarRetorno(String nomeArquivo) {
//        if (!isLogged) {
//            enviarResposta("530 - Não logado");
//            return;
//        }

        File arquivo = new File(diretorioAtual + separadorArquivo + nomeArquivo); // arquivo a ser transferido

        if (!arquivo.exists() && !arquivo.isFile()) {
            enviarResposta("550 - Arquivo não encontrado"); // responde com erro se o arquivo não for encontrado
            return;
        }

        if(serverSocketDados == null || serverSocketDados.isClosed()){
            enviarResposta("421 - Serviço não disponível");
            return;
        }

        enviarResposta("150 - Abrindo conexão de dados em modo binário para transferência de arquivo");

        try {
            socketDados = serverSocketDados.accept();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(arquivo));
            BufferedOutputStream saidaDados = new BufferedOutputStream(socketDados.getOutputStream());

            System.out.println("##### DIRETORIO ATUAL " + diretorioAtual.getAbsolutePath());

            byte[] buffer = new byte[4096];
            int bytesLidos;

            // envia o arquivo em blocos de bytes
            while ((bytesLidos = bufferedInputStream.read(buffer)) != -1) {
                saidaDados.write(buffer, 0, bytesLidos);
            }

            bufferedInputStream.close();
            saidaDados.close();
            socketDados.close();

            enviarResposta("226 - Transferência concluída");

        } catch (IOException e) {
            enviarResposta("550 - Falha ao ler arquivo");
            e.printStackTrace();
        }

    }

    // metodo para armazenar o arquivo no servidor
    private void tratarArmazenar(String nomeArquivo) {
        if (!isLogged) {
            enviarResposta("530 - Não logado");
            return;
        }

        if (nomeArquivo == null || nomeArquivo.isEmpty()) {
            enviarResposta("501 - Nome do arquivo não fornecido");
            return;
        }

        File arquivo = new File(diretorioAtual, nomeArquivo); // arquivo a ser recebido

        if(arquivo.exists()){
            enviarResposta("550 - Arquivo já existe.");
            return;
        }

        if(serverSocketDados == null || serverSocketDados.isClosed()){
            enviarResposta("421 - Serviço não disponível");
            return;
        }

        enviarResposta("150 - Abrindo conexão de dados em modo binário para transferência de arquivo");

        try {
            socketDados = serverSocketDados.accept();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(arquivo));
            BufferedInputStream entradaDados = new BufferedInputStream(socketDados.getInputStream());

            byte[] buffer = new byte[1024];
            int bytesLidos;

            // recebe o arquivo em blocos de bytes
            while ((bytesLidos = entradaDados.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesLidos);
                bufferedOutputStream.flush();
            }

            entradaDados.close();
            bufferedOutputStream.close();
            socketDados.close();

            enviarResposta("226 - Transferência concluída");

        } catch (IOException e) {
            enviarResposta("550 - Falha ao receber ou gravar arquivo");
            e.printStackTrace();
        }
    }

    private void tratarSair() {

        enviarResposta("221 - Adeus");

        try {
            socketControle.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tratarMudarDiretorio(String diretorio) {
        if (!isLogged) {
            enviarResposta("530 - Não logado");
            return;
        }

        File novoDiretorio = new File(diretorio);

        if (novoDiretorio.exists() && novoDiretorio.isDirectory()) {

            diretorioAtual = novoDiretorio;

            System.setProperty("user.dir", novoDiretorio.getAbsolutePath());

            enviarResposta("250 - Diretório alterado para " + novoDiretorio.getAbsolutePath());

        } else {
                enviarResposta("550 - Falha ao mudar diretório");
        }
    }

    private void tratarImprimirDiretorio() {
        if (!isLogged) {
            enviarResposta("530 - Não logado");
            return;
        }

        // envia mensagem com o diretório atual
        enviarResposta("257 - \"" + System.getProperty("user.dir") + "\"");
    }

    private void tratarAjuda() {
        StringBuilder resposta = new StringBuilder();
        resposta.append("214 - Comandos disponíveis:\n");
        resposta.append(" USER <nome_usuario> - Envia o nome de usuário para o servidor.\n");
        resposta.append(" PASS <senha> - Envia a senha do usuário para autenticação.\n");
        resposta.append(" LIST - Lista os arquivos no diretório atual.\n");
        resposta.append(" RETR <nome_arquivo> - Faz o download de um arquivo do servidor.\n");
        resposta.append(" STOR <nome_arquivo> - Faz o upload de um arquivo para o servidor.\n");
        resposta.append(" QUIT - Encerra a sessão FTP.\n");
        resposta.append(" CWD <diretorio> - Muda o diretório de trabalho no servidor.\n");
        resposta.append(" PWD - Exibe o diretório de trabalho atual.\n");
        resposta.append("214 - Fim da lista de comandos");

        enviarResposta(resposta.toString());
    }

    private void fecharConexoes() {
        try {
            if (socketControle != null) socketControle.close();
            if (entradaControle != null) entradaControle.close();
            if (saidaControle != null) saidaControle.close();
            if (serverSocketDados != null) serverSocketDados.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean tratarPASV() throws IOException {
        try {
            serverSocketDados = new ServerSocket(0);

            int portaDados = serverSocketDados.getLocalPort();

            int p1 = portaDados / 256;
            int p2 = portaDados % 256;

            enviarResposta("227 - Conexão estabelecida para o modo passivo (127,0,0,1," + p1 + "," + p2 + ")");
            return true;

        } catch (IOException e) {
            enviarResposta("425 - Não foi possível estabelecer a conexão para transferência dos dados.");
            System.out.println("Erro ao iniciar o socket de dados: " + e.getMessage());
            return false;
        }
    }

    // metodo responsável por enviar a resposta para o cliente
    private void enviarResposta(String resposta) {
        saidaControle.println(resposta);
        saidaControle.flush(); // Assegura que todos os dados foram enviados
        System.out.println("Thread " + Thread.currentThread().getId() + " - Resposta enviada: " + resposta); // Log da resposta enviada
    }
}