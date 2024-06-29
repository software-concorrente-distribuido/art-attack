package br.ufg.artattack.modelo;

public enum TipoPermissao {
    VISUALIZAR("VISUALIZAR"),
    EDITAR("EDITAR"),

    EXCLUIR("EXCLUIR");
    private String valor;
    TipoPermissao(String tipo){
        this.valor = tipo;
    }

    @Override
    public String toString() {
        return this.valor;
    }
}
