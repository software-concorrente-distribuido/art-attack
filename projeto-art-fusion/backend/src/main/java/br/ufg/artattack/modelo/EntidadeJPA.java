package br.ufg.artattack.modelo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@MappedSuperclass
@Getter
@Setter
public abstract class EntidadeJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;




    public Date dataCriacao;


    public Long getId() {
        return id;
    }

}
