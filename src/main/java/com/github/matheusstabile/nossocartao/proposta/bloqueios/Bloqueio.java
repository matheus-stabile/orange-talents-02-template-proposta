package com.github.matheusstabile.nossocartao.proposta.bloqueios;

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
public class Bloqueio {
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
    public Bloqueio() {
    }

    public Bloqueio(@NotNull String ipCliente, @NotNull String userAgent) {
        Assert.isTrue(StringUtils.hasText(ipCliente), "ip do cliente não pode estar em branco");
        Assert.isTrue(StringUtils.hasText(userAgent), "user agent não pode estar em branco");

        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }
}
