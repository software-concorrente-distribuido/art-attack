package com.artattack.simulator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class ProducerConsumerSimulator extends Application {
    private static final int BUFFER_SIZE = 4;
    private int count = 0;
    private Rectangle[] bufferRectangles;
    private TextFlow[] producerCodeTexts;
    private TextFlow[] consumerCodeTexts;
    private int producerLineNumber = -1; // Linha do produtor a ser executada
    private int consumerLineNumber = -1; // Linha do consumidor a ser executada
    private TextArea messageArea;
    private Text countText;
    private Button producerButton;
    private Button consumerButton;
    private Label statusLabelConsumer;
    private Label statusLabelProducer;
    private boolean isProducerSleeping = false;
    private boolean isConsumerSleeping = false;
    private int clicConsumer = 0;
    private int clicProducer = 0;


    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox producerBox = new VBox(10);
        VBox consumerBox = new VBox(10);
        GridPane bufferGrid = new GridPane();

        producerCodeTexts = createCodeTexts(getProducerCode());
        consumerCodeTexts = createCodeTexts(getConsumerCode());

        producerButton = new Button("Executar Produtor");
        consumerButton = new Button("Executar Consumidor");

        producerButton.setOnAction(e -> executeProducerCode());
        consumerButton.setOnAction(e -> executeConsumerCode());

        // Definindo a fonte e tamanho desejados
        Font font = Font.font("Arial", FontWeight.BOLD, 16);

        // Criando os labels
        Label producerLabel = new Label("Produtor");
        Label consumerLabel = new Label("Consumidor");

        // Aplicando a fonte e tamanho aos labels
        producerLabel.setFont(font);
        consumerLabel.setFont(font);

        // Centralizando os labels
        producerLabel.setAlignment(Pos.CENTER);
        consumerLabel.setAlignment(Pos.CENTER);

        producerButton.setAlignment(Pos.CENTER);

        // Criação do label
        statusLabelProducer = new Label("Pronto!");
        statusLabelProducer.setFont(Font.font("Verdana", FontPosture.ITALIC, 14));
        statusLabelProducer.setTextFill(Color.BLUE);

        statusLabelConsumer = new Label("Pronto!");
        statusLabelConsumer.setFont(Font.font("Verdana", FontPosture.ITALIC, 14));
        statusLabelConsumer.setTextFill(Color.BLUE);

        // Adicionando os elementos aos seus respectivos grids
        producerBox.getChildren().addAll(producerLabel, createCodePane(producerCodeTexts), producerButton, statusLabelProducer);
        consumerBox.getChildren().addAll(consumerLabel, createCodePane(consumerCodeTexts), consumerButton, statusLabelConsumer);

        Label bufferLabel = new Label("Buffer");
        bufferLabel.setFont(Font.font("Verdana", 14));
        bufferRectangles = createBufferRectangles();
        bufferGrid.addRow(0, bufferRectangles);
        bufferGrid.setAlignment(Pos.CENTER);

        HBox bufferBox = new HBox(10);
        bufferBox.getChildren().addAll(bufferGrid);
        bufferBox.setAlignment(Pos.CENTER);

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setPrefRowCount(10);
        messageArea.setWrapText(true);

        Label messageLabel = new Label("Mensagens");
        messageLabel.setFont(font);
        Insets padding = new Insets(15, 0, 0, 10); // 10 pixels de espaçamento em todos os lados
        messageLabel.setPadding(padding);

        countText = new Text("Count: " + count);
        countText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        VBox centerBox = new VBox(20, bufferLabel, bufferBox, countText);
        centerBox.setAlignment(Pos.CENTER);

        producerBox.setPadding(new Insets(30));
        producerBox.setStyle("-fx-background-color: #f0f0f0;");
        consumerBox.setPadding(new Insets(30));
        consumerBox.setStyle("-fx-background-color: #f0f0f0;");
        messageArea.setPrefHeight(100);
        messageArea.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14;");
        root.setStyle("-fx-background-color: #e0e0e0;");

        Label title = new Label("Simulador: Produtor-consumidor com Sleep e Wakeup");
        title.setStyle("-fx-text-fill: blue; -fx-font-size: 24px; -fx-padding: 20px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        root.setTop(titleBox);

        root.setLeft(producerBox);
        root.setCenter(centerBox);
        root.setRight(consumerBox);
        root.setBottom(new VBox(5, messageLabel, messageArea));
        BorderPane.setAlignment(producerBox, Pos.CENTER);
        BorderPane.setAlignment(centerBox, Pos.CENTER);
        BorderPane.setAlignment(consumerBox, Pos.CENTER);

        Scene scene = new Scene(root, 1100, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulador - Produtor-Consumidor com Sleep e Wakeup");
        primaryStage.show();
    }

    private VBox createCodePane(TextFlow[] codeTexts) {
        VBox codePane = new VBox();
        codePane.setPadding(new Insets(30));
        codePane.setStyle("-fx-background-color: #f8f8f8;");
        for (TextFlow text : codeTexts) {
            codePane.getChildren().add(text);
        }
        return codePane;
    }

    private TextFlow[] createCodeTexts(String[] code) {
        TextFlow[] codeTexts = new TextFlow[code.length];
        Font lineNumberFont = Font.font("Verdana", FontWeight.LIGHT, 12); // Tamanho da fonte para o número da linha

        for (int i = 0; i < code.length; i++) {
            // Criando o texto para o número da linha
            Text lineNumberText = new Text(String.valueOf(i + 1));
            lineNumberText.setFont(lineNumberFont);

            // Criando o texto para o código da linha
            Text codeLineText = new Text(code[i]);
            codeLineText.setFont(Font.font("Verdana", FontWeight.NORMAL, 16)); // Alteração da fonte para tamanho 16
            codeLineText.setStyle("-fx-fill: #333333;");

            // Adicionando o número da linha e o código em um TextFlow
            TextFlow textFlow = new TextFlow(lineNumberText, new Text(" "), codeLineText);
            codeTexts[i] = textFlow;
        }

        return codeTexts;
    }


    private void executeProducerCode() {

        int toBeExecuted = 0;

        statusLabelProducer.setText("Executando.");

        // Se a linha atual for -1 ou a última linha, reinicia a execução do código
        if (producerLineNumber == -1 || producerLineNumber == producerCodeTexts.length) {
            producerLineNumber = 2;
        }

        clicProducer++;

        if(clicProducer == 1){
            toBeExecuted = producerLineNumber+1;
            clearMessage();
            showMessage("PRODUTOR - Linha a ser executada: " + toBeExecuted);
            updateCodeHighlights(producerCodeTexts, producerLineNumber, Color.RED);
            return;
        } else if(clicProducer == 2) {
            clicProducer = 0;
            updateCodeHighlights(producerCodeTexts, producerLineNumber, Color.BLUE);
        }

        // Executa a lógica correspondente à linha atual
        switch (producerLineNumber) {
            case 2:
                clearMessage();
                showMessage("Looping");
                producerLineNumber = 3;
                break;
            case 3:
                clearMessage();
                showMessage("PRODUTOR - Executou linha 4: Produz item");
                producerLineNumber = 4;
                break;
            case 4:
                clearMessage();
                if (count == BUFFER_SIZE) {
                    showMessage("PRODUTOR - Executou linha 5: Verificando se o buffer está cheio");
                    producerLineNumber = 5;
                }
                else {
                    showMessage("PRODUTOR - Executou linha 5:  O Buffer não está cheio, permitido adicionar o item");
                    producerLineNumber = 6;
                }
                break;
            case 5:
                clearMessage();
                showMessage("PRODUTOR - Executou linha 6");
                showMessage("Produtor: O buffer está cheio, dormindo...");
                disableProducer();
                break;
            case 6:
                clearMessage();
                showMessage("PRODUTOR - Executou linha 7: adicionando item ao buffer");
                produceOnGrid(count);
                producerLineNumber = 7;
                break;
            case 7:
                clearMessage();
                showMessage("PRODUTOR - Executou linha 8: atualizando o valor da variável count");
                count++;
                updateCountText();
                producerLineNumber = 8;
                break;
            case 8:
                if (count == 1) {
                    producerLineNumber = 9;
                } else {
                    producerLineNumber = -1;
                }
                clearMessage();
                showMessage("PRODUTOR - Executou linha 9: Verificando se count = 1");
                break;
            case 9:
                producerLineNumber = 10;
                clearMessage();
                if(!isConsumerSleeping) {
                    showMessage("PRODUTOR - Executou Linha 9");
                    showMessage("Produtor: O consumidor não está dormindo, o sinal será perdido.");
                } else {
                    enableConsumer();
                    showMessage("PRODUTOR -> Executou Linha 8: Produtor acordando o consumidor.");
                    showMessage("Produtor: Avisando o consumidor.");
                }
                break;
        }
    }

    private void executeConsumerCode() {

        statusLabelConsumer.setText("Executando.");
        int toBeExecuted = 0;

        // Se a linha atual for -1 ou a última linha, reinicia a execução do código
        if (consumerLineNumber == -1 || consumerLineNumber == producerCodeTexts.length) {
            consumerLineNumber = 2;
        }

        clicConsumer++;

        if(clicConsumer == 1){
            toBeExecuted = consumerLineNumber+1;
            clearMessage();
            showMessage("Consumidor - Linha a ser executada: " + toBeExecuted);
            updateCodeHighlights(consumerCodeTexts, consumerLineNumber, Color.RED);
            return;
        } else if(clicConsumer == 2) {
            clicConsumer = 0;
            // Atualiza a aparência do código destacando a linha atual
            updateCodeHighlights(consumerCodeTexts, consumerLineNumber, Color.BLUE);
        }

        // Executa a lógica correspondente à linha atual
        switch (consumerLineNumber) {
            case 2:
                clearMessage();
                showMessage("CONSUMIDOR - Looping");
                consumerLineNumber = 3;
                break;
            case 3:
                if (count == 0) {
                    clearMessage();
                    showMessage("CONSUMIDOR - Executou linha 4: Verificando se o buffer está vazio");
                    consumerLineNumber = 4;
                }
                else {
                    clearMessage();
                    showMessage("CONSUMIDOR - Executou linha 4:  O Buffer não está vazio, permitido consumir o item");
                    consumerLineNumber = 5;
                }
                break;
            case 4:
                clearMessage();
                showMessage("CONSUMIDOR - Executou linha 5");
                showMessage("Consumidor: O buffer está vazio, dormindo...");
                disableConsumer();
                break;
            case 5:
                clearMessage();
                showMessage("CONSUMIDOR - Executou linha 6: consumindo o item do buffer");
                consumerLineNumber = 6;
                bufferRectangles[count - 1].setFill(Color.WHITE);
                bufferRectangles[count - 1].setStroke(Color.BLUE);
                break;
            case 6:
                clearMessage();
                showMessage("CONSUMIDOR - Executou linha 7: atualizando o valor da variável count");
                count--;
                updateCountText();
                consumerLineNumber = 7;
                break;
            case 7:
                clearMessage();
                showMessage("CONSUMIDOR - Executou Linha 8");
                if (count == BUFFER_SIZE - 1) {
                    consumerLineNumber = 8;
                } else {
                    consumerLineNumber = 9;
                }
                break;
            case 8:
                clearMessage();
                if(!isProducerSleeping) {
                    showMessage("CONSUMIDOR - Executou Linha 9");
                    showMessage("O produtor não está dormindo, o sinal será perdido.");
                } else {
                    enableProducer();
                    statusLabelProducer.setText("Executando.");
                    showMessage("CONSUMIDOR - Executou Linha 9");
                    showMessage("Consumidor: Avisando o produtor.");
                }
                consumerLineNumber = 9;
                break;
            case 9:
                consumerLineNumber = 10;
                showMessage("CONSUMIDOR - Executou Linha 10: consumindo item");
                break;

        }
    }

    private void produceOnGrid(int count) {
        for (int i = 0; i <= count; i++) {
            bufferRectangles[i].setFill(Color.BLUE);
            bufferRectangles[i].setStroke(Color.BLACK);
        }
    }

    public void disableProducer() {
        isProducerSleeping = true;
        producerButton.setDisable(true);
        statusLabelProducer.setText("Aguardando o sinal...");
    }

    public void enableProducer() {
        statusLabelProducer.setText("Executando.");
        isProducerSleeping = false;
        producerButton.setDisable(false);
        producerLineNumber = 6;
    }

    public void disableConsumer() {
        isConsumerSleeping = true;
        consumerButton.setDisable(true);
        statusLabelConsumer.setText("Aguardando o sinal...");
    }

    public void enableConsumer() {
        // Desativa o botão do produtor
        isConsumerSleeping = false;
        consumerButton.setDisable(false);
        statusLabelConsumer.setText("Executando.");
        consumerLineNumber = 5;
    }

    private void updateCodeHighlights(TextFlow[] codeTexts, int lineNumber, Color c) {
        for (int i = 0; i < codeTexts.length; i++) {
            if (i == lineNumber) {
                // Supondo que 'textFlow' seja o seu TextFlow
                for (Node node : codeTexts[i].getChildren()) {
                    if (node instanceof Text) {
                        ((Text) node).setFill(c); // Define a cor do texto para vermelho
                    }
                }
            } else {
                for (Node node : codeTexts[i].getChildren()) {
                    if (node instanceof Text) {
                        ((Text) node).setFill(Color.BLACK); // Define a cor do texto para vermelho
                    }
                }
            }
        }
    }

    private void showMessage(String message) {
        messageArea.appendText(message + "\n");
    }

    private void clearMessage() {
        messageArea.clear();
    }

    private void updateCountText() {
        countText.setText("Count: " + count);
    }

    private String[] getProducerCode() {
        return new String[]{
                "   # Define N " + BUFFER_SIZE,
                " ",
                "   Enquanto verdadeiro:",
                "       item = produzir_item()",
                "       se count == N:",
                "            dormir()",
                "       buffer.adicionar(item)",
                "       count = count + 1",
                "       se count == 1:",
                "           acordar_consumidor()"
        };
    }

    private String[] getConsumerCode() {
        return new String[]{
                "   # Define N " + BUFFER_SIZE,
                " ",
                "   Enquanto verdadeiro:",
                "       se count == 0:",
                "            dormir()",
                "       item = buffer.remover()",
                "       count = count - 1",
                "       se count == N - 1:",
                "            acordar_produtor()",
                "       consumir_item(item)"
        };
    }

    private Rectangle[] createBufferRectangles() {
        Rectangle[] rectangles = new Rectangle[BUFFER_SIZE];
        for (int i = 0; i < BUFFER_SIZE; i++) {
            rectangles[i] = new Rectangle(50, 50);
            rectangles[i].setFill(Color.WHITE);
            rectangles[i].setStroke(Color.BLACK);
        }
        return rectangles;
    }

}

