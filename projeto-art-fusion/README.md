## Trabalho Final da Disciplina de Software Concorrente e Distribuído!

## Diagramas
[CLIQUE AQUI](https://drive.google.com/file/d/1nSkpeHJiGiVqusFc7uL7A-Jva1d9B7br/view?usp=sharing)

## Software Design Document
[CLIQUE AQUI](https://docs.google.com/document/d/1geOp2d0BULVQ4s8E5dZOnip0_Pg6LtLSr_oZa_zrrno/edit?usp=sharing)


## Guia de execução do backend:

- JDK 21
- Tenha um postgres com um banco de dados chamado "art-attack" criado.
- Crie um arquivo chamado `application-dev.properties` e coloque as seguintes configurações nele:
	spring.datasource.username=<seu_username_local> 
	spring.datasource.password=<sua_senha_local>
	
- Sete o Active Profile do IntelliJ para "dev". 	

## Guia de execução do frontend:

#### Pré-requisitos
- Ter instalado o **Node.js**
- Ter instalado o gerenciador de pacotes **npm**

#### Instalação
- Clonar o repositório do projeto usando Git
```
git clone https://github.com/software-concorrente-distribuido/art-attack
```
  
- Navegar até o diretório `/projeto-art-fusion/artfusion-front`
- Instalar as dependências:
```
npm install
```
  
- Iniciar o servidor de desenvolvimento
```
npm start
```
  
- O servidor estará disponível no endereço http://localhost:3000
