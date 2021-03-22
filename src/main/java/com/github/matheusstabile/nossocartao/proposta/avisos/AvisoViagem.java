package com.github.matheusstabile.nossocartao.proposta.avisos;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime instante;

    @NotNull
    private String ipCliente;

    @NotNull
    private String userAgent;

    @NotNull
    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public AvisoViagem() {
    }

    public AvisoViagem(@NotNull String ipCliente, @NotNull String userAgent, Cartao cartao) {
        Assert.state(StringUtils.hasText(ipCliente), "ipCliente não pode estar em branco");
        Assert.state(StringUtils.hasText(userAgent), "userAgent não pode estar em branco");
        Assert.notNull(cartao, "cartão não pode ser nulo");

        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
