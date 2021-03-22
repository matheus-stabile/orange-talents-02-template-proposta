package com.github.matheusstabile.nossocartao.proposta.carteiras;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoCarteira tipo;

    @NotNull
    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Carteira() {
    }

    public Carteira(@NotNull String email, @NotNull TipoCarteira tipo, @NotNull Cartao cartao) {
        Assert.notNull(cartao, "cartão não pode ser nulo");
        Assert.isTrue(StringUtils.hasText(email), "email não pode estar em branco");
        Assert.notNull(tipo, "tipo da carteira não pode ser nulo");

        this.email = email;
        this.tipo = tipo;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public TipoCarteira getTipo() {
        return tipo;
    }
}
