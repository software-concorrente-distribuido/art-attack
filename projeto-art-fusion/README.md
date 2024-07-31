# Trabalho Final da Disciplina de Software Concorrente e Distribuído!

## ArtFusion
O ArtFusion é uma plataforma online de criação colaborativa de arte, projetada para artistas de todas as habilidades e estilos. Com o ArtFusion, os usuários podem criar, colaborar e compartilhar obras de arte digital em tempo real, conectando-se com outros artistas em todo o mundo. Com uma interface intuitiva e uma variedade de ferramentas de desenho e pintura, a plataforma permite que os usuários expressem sua criatividade de maneira flexível e dinâmica.

## Diagramas
[CLIQUE AQUI](https://drive.google.com/file/d/1nSkpeHJiGiVqusFc7uL7A-Jva1d9B7br/view?usp=sharing)

## Software Design Document
[CLIQUE AQUI](https://docs.google.com/document/d/1geOp2d0BULVQ4s8E5dZOnip0_Pg6LtLSr_oZa_zrrno/edit?usp=sharing)


## Guia de execução do backend:

### Pré-requisitos
- **Java 21**: Assegure-se de que a JDK 21 está instalada e configurada corretamente em seu sistema.
- Ter o Docker instalado.
- Tenha um postgres com um banco de dados chamado "art-attack" criado.

### Instalação
- Clonar o repositório do projeto usando Git:
```
git clone https://github.com/software-concorrente-distribuido/art-attack
```

- Navegar até o diretório `/projeto-art-fusion/backend` ou abrir o projeto em uma IDE de sua preferência.
  
- No diretório `/projeto-art-fusion/backend`, abra o terminal e use o Docker Compose para iniciar o RabbitMQ:
```
docker-compose up -d
```
- Editar o `application-dev.properties` e alterar as seguintes configurações:
```
spring.datasource.username=<seu_username_local> 
spring.datasource.password=<sua_senha_local>
```
	
- Sete o Active Profile do IntelliJ para "dev".

- Execute a aplicação.

## Guia de execução do frontend:

### Pré-requisitos
- **Node.js e npm**: Certifique-se de ter o Node.js e npm instalados. Instalar [Node.js](https://nodejs.org/pt/download/package-manager) e [npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm).

### Instalação
  
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
