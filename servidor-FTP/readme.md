# Servidor FTP em JAVA

Este é um servidor FTP simples, implementado em Java.

O servidor FTP implementado é multi-threaded, o que significa que múltiplos usuários podem transferir arquivos simultaneamente. No entanto, o acesso ao sistema de arquivos não é sincronizado. Isso implica que se dois clientes tentarem escrever no mesmo arquivo ao mesmo tempo, o resultado será inválido, pois as escritas podem se sobrepor ou corromper os dados, levando a inconsistências no arquivo final.

### Arquitetura de Threads Trabalhadoras no Servidor FTP

No contexto do servidor FTP implementado, uma "thread trabalhadora" é uma thread dedicada que lida com todas as interações com um cliente específico desde o momento que se conecta até o encerramento da sessão. Neste modelo, cada conexão de cliente é isoladamente gerenciada por uma única thread. Este modelo é eficiente para garantir que cada cliente possa operar de forma independente sem afetar as operações de outros clientes conectados ao servidor.

**1. Criação de Threads:**
  
  Quando um cliente se conecta ao servidor na porta principal (por padrão, a porta 21), o ServerSocket do servidor aceita essa conexão, criando um Socket para a sessão de controle.

  Imediatamente após aceitar a conexão, o servidor delega o tratamento dessa sessão a uma nova thread, instanciando a classe Trabalhador com o Socket recém-criado e iniciando-a.

**2. Classe Trabalhador:**
  
  A classe `Trabalhador` implementa a interface `Runnable`e dentro do método `run()`, a thread configura streams de entrada e saída para comunicar com o cliente através do Socket de controle. Os comandos enviados pelo cliente são lidos e interpretados nesta thread, e as respostas apropriadas são enviadas de volta ao cliente através do mesmo Socket.

**3. Gerenciamento de Conexões de Dados:**
  
  Para transferências de arquivos e listagem de diretórios, cada thread pode abrir conexões de dados separadas em modo passivo (PASV). A thread trabalhadora escuta em uma nova porta de dados e comunica essa porta ao cliente.

  A conexão de dados é estabelecida apenas quando necessária e é fechada após a conclusão da transferência, sendo este processo também gerenciado isoladamente pela thread trabalhadora.


## Comandos implementados

Comandos Suportados

Abaixo estão os comandos FTP suportados pelo servidor, junto com suas descrições e parâmetros:

1. **USER `<username>`**:
- **Descrição**: Autentica o usuário com o servidor.
- **Parâmetros**:
  - `username`: Nome do usuário para autenticação.

2. **PASS `<password>`**:
- **Descrição**: Envia a senha para autenticação após o comando USER.
- **Parâmetros**:
  - `password`: Senha do usuário.

3. **LIST:**
- Descrição:  Lista os arquivos no diretório atual.

4. **RETR `<filename>`**: 
- **Descrição**: Transfere um arquivo do servidor para o cliente.
- **Parâmetros**:
  - `filename`: Nome do arquivo a ser transferido.

5. **STOR `<filename>`**:
- **Descrição**: Transfere um arquivo do cliente para o servidor.
- **Parâmetros**:
  - `filename`: Nome do arquivo a ser armazenado no servidor.

6. **QUIT**:
- Descrição: Encerra a sessão do cliente com o servidor.

7. **CWD `<directory>`**:
- **Descrição**: Muda o diretório atual no servidor.
- **Parâmetros**:
  - `directory`: Caminho do novo diretório de trabalho.

8. **PWD**:
- **Descrição**: Mostra o diretório de trabalho atual.

9. **PASV**:
- **Descrição**: Inicia o modo passivo, abrindo uma nova porta para a transferência de dados.

10. **HELP**:
- **Descrição**: Lista todos os comandos suportados pelo servidor.

## Respostas enviadas pelo servidor

Abaixo estão as respostas específicas que o servidor pode enviar em resposta a ações e comandos dos clientes. Essas respostas ajudam os clientes a entender o estado do servidor e a tomar ações apropriadas:

- `220 - Bem-vindo ao Servidor FTP.`: Indica que a conexão foi estabelecida com sucesso.
- `331 - Nome de usuário OK, precisa de senha`: Solicita a senha após o comando USER ser aceito.
- `530 - Nome de usuário inválido`: Erro quando o nome de usuário fornecido é desconhecido ou errado.
- `530 - Senha inválida`: Erro quando a senha fornecida não corresponde ao nome de usuário.
- `230 - Usuário logado`: Sucesso na autenticação.
- `502 - Comando não implementado`: O comando enviado pelo cliente não é suportado pelo servidor.
- `550 - Arquivo não encontrado`: Erro quando o arquivo especificado não é encontrado durante operações RETR ou DELE.
- `226 - Transferência concluída`: Sucesso na conclusão de uma transferência de arquivo.
- `150 - Abrindo conexão de dados em modo ASCII para lista de arquivos`: Preparação para iniciar a transferência de listagem de arquivos.
- `150 - Abrindo conexão de dados em modo binário para transferência de arquivo`: Preparação para iniciar a transferência de arquivo.
- `425 - Não foi possível estabelecer conexão de dado`s: Falha ao tentar abrir uma conexão de dados.
- `221 - Adeus`: Resposta ao comando QUIT, indicando que a conexão será fechada.
- `257 - "Caminho_atual"`: Resposta ao comando PWD mostrando o diretório atual.
- `501 - Nome do arquivo não fornecido`: Erro quando um comando requer um nome de arquivo, mas ele não é fornecido.
- `550 - Arquivo já existe.`: Erro quando tenta-se criar um arquivo que já existe.
- `421 - Serviço não disponível`: Erro genérico para operações falhas que não permitem continuar.
- `250 - Diretório alterado para "Caminho_novo"`: Sucesso ao mudar o diretório de trabalho.

## Usando o Servidor

Para usar o servidor FTP para transferências de arquivos, os clientes devem:

1. Conectar-se ao servidor usando um cliente FTP.
2. Autenticar-se com comandos USER e PASS.
3. Para utilizar os comandos que necessitam transferência de dados, como LIST, RETR e STOR, o cliente deve conectar-se à porta de dados fornecida após o comando PASV.

### Usando o Servidor após PASV

Após enviar o comando PASV e receber a porta de dados do servidor, o cliente deve realizar as seguintes ações para efetuar transferências de dados:

**1. Conectar-se à Porta de Dados:**
- Após receber a resposta 227, o cliente deve extrair os números IP e a porta de dados da resposta. Essa resposta típica será algo como `227 - Conexão estabelecida para o modo passivo (127,0,0,1,p1,p2)`, onde `p1` e `p`2 são usados para calcular a porta `(porta = p1 * 256 + p2)`.
- Usando essas informações, o cliente estabelece uma nova conexão Socket diretamente à porta de dados indicada.

**2. Enviar ou Receber Dados:**
- Para baixar um arquivo (RETR): O cliente envia o comando `RETR nome_do_arquivo`. O servidor começará a enviar o arquivo pela porta de dados. O cliente deve ler esses dados e salvá-los localmente. 
- Para enviar um arquivo (STOR): O cliente envia o comando `STOR nome_do_arquivo`. Em seguida, o cliente deve enviar o conteúdo do arquivo pela conexão de dados. O servidor lerá esses dados da porta de dados e salvará o arquivo no servidor.
- Para listar arquivos (LIST): O cliente envia o comando LIST, e o servidor responde enviando a lista de arquivos e diretórios no diretório atual através da conexão de dados.

  **Importante**: antes de iniciar qualquer operação de transferência, é crucial verificar a resposta do servidor; por exemplo, se ao solicitar um download com `RETR`, a resposta for `150`, proceda, mas se for `550`, interrompa a operação, pois indica que o arquivo não está disponível ou há um problema de acesso.

**3. Encerrar a Conexão de Dados:**
- Após a transferência de dados ser concluída, o cliente deve fechar a conexão de dados e os streams de transferência. O servidor também fechará essa conexão de seu lado após completar a operação solicitada.
