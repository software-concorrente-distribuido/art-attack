package br.ufg.artattack.modelo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@MappedSuperclass
@Getter
@Setter
public abstract class EntidadeJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "is_ativo")
    private boolean isAtivo;

    public Long getId() {
        return id;
    }

}
