# Parte 2

## Questão 1.
Uma arquitetura que fornece uma possível solução é a implementada no código. Ela vem
da visão do problema como um problema clássico de Buffer Limitado, porém com o buffer
com um só espaço, que é o da mensagem! A arquitetura funciona da seguinte forma:
todos os consumidores tentam sempre consumir, se não conseguirem, esperam até
receberem um sinal de algum produtor de que a mensagem está na caixa, então eles 
tentam novamente fazer a operação de consumir, e assim recursivamente. Para o 
produtor é o mesmo caso, sempre tentam produzir, se não conseguirem, esperam
até que algum consumidor os sinalize que existe uma mensagem na caixa, nesse
momento é repetida a operação. Porém, na implementação, quem realiza essa lógica
é a própria MailBox, que controla os próprios acessos, fazendo quem quer consumir
esperar na hora certa e quem quer produzir, produzir a mensagem na hora certa!
```
MailBox(){
   String msg
    storeMessage(mensagem){
    if(msg==null){
     msg = mensagem;
     }
    else{
     wait();
     this.storeMessage(mensagem);
     }
    }
    retrieveMessage(){
    if(msg!=null){
     String msgRetornar = new String(msg);
     msg = null;
     return msgRetornar;
    }else{
    wait()
    return this.retrieveMessage();
    }
    }
}

```

## Questão 2.

A figura ilustra o funcionamento de dois processos concorrentes, ou seja, que executam tarefas e compartilham uma região crítica. No T1, o processo A entra em sua região crítica, uma seção do código que acessa recursos compartilhados e deve ser executada exclusivamente por um processo de cada vez. Logo, ao entrar na região crítica, o processo A obtém o controle dos recursos compartilhados, impedindo outros processos de acessá-los ao mesmo tempo. Assim, no T2, o processo B tenta entrar na região crítica, mas é bloqueado, pois o processo A ainda está ocupando a região crítica. No T3, quando o processo A deixa a região crítica, o processo B consegue o acesso.

Conceitos:

- **Processo**: Uma instância de um programa em execução, incluindo seu código, dados e contexto de execução.
- **Região crítica**: Seção do código que acessa recursos compartilhados e deve ser executada exclusivamente por uma thread ou processo de cada vez.
- **Concorrência**: Execução de múltiplas tarefas simultaneamente, compartilhando recursos e coordenando o acesso a esses recursos para evitar conflitos.
- **Exclusão mútua**: garantia de que apenas um processo ou thread pode acessar uma seção crítica do código ou um recurso compartilhado por vez, evitando conflitos e inconsistências.

## Questão 3.

Como prós, a solução é simples e resolve o problema de concorrência, coordenando o acesso dos processos à  região crítica por meio da variável "turn". Entretanto, a solução pode apresentar problemas como o da espera ocupada, em que o processo consome os recursos da CPU enquanto aguarda. Além disso, o processo pode entrar na sua região crítica e não conseguir sair, enquanto o outro processo ficará esperando indefinidamente sua vez para entrar na região critica, problema conhecido como starvation. Ainda, a solução não é escalável para mais de um processo.

Outras soluções para esse problema:

- **Sleep e wakeup**: primitivas de comunicação entre processos que evita o problema da espera ocupada.
- **Semáforos**: mecanismos de sincronização concorrentes e distribuídos para controlar o acesso a recursos compartilhados por múltiplos processos ou threads. Os semáforos podem ser binários (mutexes) ou contadores (valor > 0).
- **Monitores**: abstrações de alto nível que encapsulam dados compartilhados e métodos de sincronização, utilizando bloqueios e variáveis de condição para gerenciar o acesso concorrente a recursos de forma segura e organizada.

## Questão 4.

Um processo é uma instância de um programa em execução, onde cada processo possui seus próprios recursos (dados, memória, o contador de programa, pilhas, registradores e área de dados). Processos são isolados uns dos outros, garantindo que a execução de um não interfira na execução de outro, mas tornando a comunicação entre eles mais complexa.

Threads são unidades básicas de execução dentro de um processo. Threads de um mesmo processo compartilham o mesmo espaço de memória e recursos, permitindo a execução paralela de tarefas dentro do processo.

Um processo pode ter várias threads, e uma thread somente pode constituir um processo. Em termos de concorrência, as threads de um mesmo processo podem compartilhar e disputar os recursos da área deste processo.

## Questão 5.

Safety e liveness são propriedades essenciais na programação concorrente. Safety assegura que um programa nunca entre em um estado incorreto ou indesejado durante sua execução, prevenindo comportamentos ruins, como a interferência entre processos, deadlocks ou corrupção de dados.

Por exemplo, em um banco de dados, duas transações concorrentes não podem modificar a mesma linha simultaneamente para evitar inconsistências. Considere dois processos concorrentes, A e B, tentando acessar e modificar uma variável compartilhada saldo em uma conta bancária. O Processo A verifica que o saldo é 100, mas antes de subtrair, o Processo B também verifica que o saldo é 100. Ambos subtraem 50, resultando em um saldo final de 50, quando deveria ser 0. Sem a aplicação correta de sincronização e controle de acesso à região crítica, os processos podem causar conflitos de dados. A propriedade de safety garante que tais inconsistências não ocorram, assegurando a integridade dos dados e evitando comportamentos incorretos durante a execução concorrente.

Já a propriedade de Liveness garante que o programa continue a fazer progresso, eventualmente atingindo estados desejados, isto é, apesar dos processos aguardarem recursos ou condições, eles eventualmente conseguirão avançar, assegurando que operações importantes sejam completadas. Por exemplo, em um sistema de processamento de tarefas, onde múltiplas threads processam tarefas de uma fila, liveness garante que cada tarefa será processada eventualmente, evitando que alguma tarefa fique na fila indefinidamente.

## Questão 6.

**a)** Para exibir a porcentagem de dados baixados ao fazer download de um arquivo é necessário utilizar técnicas de concorrência, pois para evitar que a atualização da interface gráfica bloqueie o download, é necessário usar um processo separado para o download e outro para atualizar a tela. Estes processos podem, por exemplo, compartilhar um buffer com a informação necessária para saber quanto do arquivo foi baixado, e por enquanto que um processo insere os bytes no buffer o outro os consome para calcular a quantidade de bytes baixados para envio para interface, formando assim um clássico problema de buffer limitado com consumidor e produtor.

**b)** Não é necessário utilizar concorrência, pois as requisições são processadas uma de cada vez pelo servidor.

**c)** Neste exemplo, a requisição de rede deve ser realizada em thread separada para evitar que a interface gráfica seja interrompida durante as requisições.

## Questão 7.
**a)** Certo. A concorrência envolve a execução de múltiplas tarefas que podem ser intercaladas e compartilham recursos.

**b)** Certo. Na concorrência, quando várias tarefas compartilham um recurso e o acesso simultâneo pode gerar conflitos, é necessário coordenar o acesso. Para isso, enquanto uma tarefa utiliza o recurso, as outras devem esperar. Para gerenciar esse processo de forma eficiente, utilizam-se técnicas como semáforos ou locks, que garantem que apenas uma tarefa acesse o recurso por vez, evitando conflitos e inconsistências.

**c)** Incorreto. A concorrência não se restringe a processos iguais ou dependentes. Ela pode envolver qualquer conjunto de tarefas que compartilham e disputam por recursos.

**d)** Incorreto. No  paralelismo, as tarefas são executadas simultaneamente e independentemente, a depender do número de núcleos da CPU, enquanto na concorrência temos a execução de múltiplas tarefas, mas que compartilham recursos e intercalam sua execução para coordenar o acesso a esses recursos.

## Questão 8.
**a)** A concorrência pode aumentar a carga de trabalho devido à necessidade de sincronização e gerenciamento de múltiplas threads/processos. Isso pode, em alguns casos, diminuir o desempenho em vez de melhorá-lo. Ainda, problemas como deadlocks e condições de corrida também podem surgir, afetando negativamente o desempenho. A melhora de desempenho com a concorrência só ocorre quando houver um tempo de espera muito grande que possa ser dividido em várias threads/processadores

**b)** O design de software concorrente é consideravelmente diferente do projeto de um sistema em apenas uma thread. As particularidades advindas de um projeto concorrente costuma ter grande impacto na arquitetura do sistema, sendo necessário planejar e implementar mecanismos de sincronização, gerenciar o acesso a recursos compartilhados e prever situações como deadlocks e condições de corrida, o que torna o projeto mais complexo.

**c)** Mesmo para problemas simples, é complexo implementar a concorrência completa e eficaz, sendo necessário gerenciar questões como sincronização, condições de corrida e gerenciamento de threads.

## Questão 9.

**a)** Considerando inicialmente que o `lastIdUsed` = 10.

As Threads A e B são executadas e ambas fazem chamadas concorrentes ao método `getNextId()`. A Thread A recebe o `ID` 11, e o `lastIdUsed` é atualizado para 11 antes que a Thread B possa obtê-lo. Em seguida, a Thread B chama `getNextId()`, incrementa `lastIdUsed` de 11 para 12 e recebe o `ID` 12. Neste caso, não há conflito porque a execução é sequencial e cada thread consegue completar a operação antes da outra começar.

Quando a Thread A chama `getNextId()` novamente, `lastIdUsed` é 12 (atualizado pela chamada anterior da Thread B). Entretanto, a Thread B recebe o `ID` 13 e a Thread A recebe o `ID` 14, pois a chamada da Thread B foi realizada em concorrência com a Thread A, e a atualização de `lastIdUsed`, e o do `ID` da Thread A, ocorreu após a obtenção do `ID` pela Thread B. 

Outra situação que pode ocorrer é ambas as threads lerem o valor 12 ao mesmo tempo, incrementar e retornar ao mesmo tempo. Devido à ausência de sincronização, elas acabam vendo o valor 12, incrementam para 13, e retornam 13. Isso ocorre porque as operações de leitura e escrita não são atômicas, resultando em uma condição de corrida. Apesar das duas threads serem executadas simultaneamente, o valor de `lastIdUsed` só é incrementado uma vez, resultando em inconsistências.

Logo, devido à falta de sincronização, existe a possibilidade de condições de corrida, onde os incrementos podem se sobrepor, resultando em valores inconsistentes.

**b)** Para evitar a condição de corrida e garantir a integridade dos dados, podemos usar sincronização para controlar o acesso à variável `lastIdUsed`.

Uma solução é adicionar um bloco synchronized ao método `getNextId()`.

```
class X {
    private int lastIdUsed;
    
    public synchronized int getNextId(String t) {
        System.out.println("Chamado por: " + t + " - Last id used: " + lastIdUsed);
        return ++lastIdUsed;
    }
}
```

Ao modificar o método `getNextId()` para ser sincronizado, utilizando a palavra-chave synchronized, garantimos que apenas uma thread por vez poderá executar esse método em uma instância específica de X. Isso significa que quando uma thread estiver executando o método `getNextId()`, todas as outras threads que tentarem acessá-lo serão bloqueadas até que a primeira thread termine sua execução.

Por exemplo, iremos considerar o `lastIdUsed` = 10, que foi utilizado pela thread B. Quando a Thread A acessar o método `getNextId()`, ela irá recebe o `ID` 11, pois o `lastIdUsed` era 10.

Novamente, a thread A consegue acesso ao método e, como o `lastIdUsed`  era 11, ela recebe o `ID` 12. Em seguida, a thread B acessa o método e ve o `lastIdUsed` como 12, recebendo então o `ID` 13, e assim sucessivamente.

Assim, mesmo as Threads ganhem o acesso aleatoriamente ao método, isso confirma que a sincronização adequada do método `getNextId()` usando synchronized garante que as atualizações do `lastIdUsed` sejam consistentes e que não ocorram condições de corrida ou valores desatualizados, pois o valor do `lastIdUsed` está sempre atualizado e não há discrepância entre o `ID` retornado e o valor atualizado do `lastIdUsed`.

