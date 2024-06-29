package br.ufg.artattack.modelo;

public enum Visibilidade{
    PUBLICO("PUBLICO"),
    PRIVADO("PRIVADO");

    private String valor;

    Visibilidade(String valor){
        this.valor = valor;
    }

}