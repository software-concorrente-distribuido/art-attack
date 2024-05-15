package br.ufg.artattack.mvc;

import jakarta.annotation.security.PermitAll;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Controller
public class Renderizador  {

    private static final String CAMINHO_INDEX_HTML = "/static/index.html";

    private ResponseEntity renderizarReact(){
        ResponseEntity resposta;
        try{
         resposta = ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(
                new ClassPathResource(CAMINHO_INDEX_HTML).getContentAsString(StandardCharsets.UTF_8)
        );

        }catch (IOException e){
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_HTML).body("<h1>No Content</h1>");
        }

        return resposta;

    }


    @GetMapping(value = {
            "/{caminho:^(?!api)(?!.*\\.[a-zA-Z0-9]+$).*$}",
            "/{caminho:^(?!api)(?!.*\\.[a-zA-Z0-9]+$).*$}/{final:^(?!api)(?!.*\\.[a-zA-Z0-9]+$).*$}",
            "/{caminho:^(?!api)(?!.*\\.[a-zA-Z0-9]+$).*$}/*/{final:^(?!api)(?!.*\\.[a-zA-Z0-9]+$).*$}"
    })
    @PermitAll
    public ResponseEntity renderizarPagina() {
        return this.renderizarReact();
    }


    @GetMapping(value = "/")
    public ResponseEntity r2() {
        return this.renderizarReact();
    }

}
