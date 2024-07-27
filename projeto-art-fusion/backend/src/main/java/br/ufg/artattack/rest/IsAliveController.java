package br.ufg.artattack.rest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class IsAliveController {


    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }


    @GetMapping("/isAlive/isAlive")
    public String isAliveAninhado(){
        return "<h1> Estou Vivo /isAlive/isAlive </h1>";
    }


}
