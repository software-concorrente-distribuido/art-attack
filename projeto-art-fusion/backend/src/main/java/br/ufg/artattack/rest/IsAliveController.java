package br.ufg.artattack.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class IsAliveController {

    @GetMapping
    public String isAlive(){
        return "<h1> Estou Vivo </h1>";
    }

    @GetMapping("/isAlive/isAlive")
    public String isAliveAninhado(){
        return "<h1> Estou Vivo /isAlive/isAlive </h1>";
    }


}
