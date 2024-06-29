package com.servidorftp;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorUsuarios {

    private static final Map<String, String> usuarios = new HashMap<>(); // Lista de usuários e senhas

    static {
        usuarios.put("user", "123");
        usuarios.put("usuario", "senha");
        usuarios.put("sarapatel", "sarapatel");
        usuarios.put("art", "attack");
    }

    public boolean validarUsuario(String usuario) {
        return usuarios.containsKey(usuario);
    }

    public boolean validarSenha(String usuario, String senha) {
        return usuarios.getOrDefault(usuario, "").equals(senha);
    }
}