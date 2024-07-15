package br.ufg.artattack.exception;

public class ProcessamentoException  extends RuntimeException{
    public ProcessamentoException(String msg){
        super(msg);
    }
    public ProcessamentoException(Exception e){
        super(e);
    }
}
