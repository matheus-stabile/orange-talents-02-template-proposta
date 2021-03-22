package com.github.matheusstabile.nossocartao.proposta.avisos;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Deprecated
    public AvisoViagem() {
    }

    public AvisoViagem(@NotNull String ipCliente, @NotNull String userAgent) {
        Assert.state(StringUtils.hasText(ipCliente), "ipCliente não pode estar em branco");
        Assert.state(StringUtils.hasText(userAgent), "userAgent não pode estar em branco");

        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }
}
