package com.github.matheusstabile.nossocartao.proposta.cartoes;

import com.github.matheusstabile.nossocartao.proposta.biometrias.Biometria;
import com.github.matheusstabile.nossocartao.proposta.bloqueios.Bloqueio;
import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String numero;

    @NotNull
    private LocalDateTime emitidoEm;

    @NotNull
    private String titular;

    @NotNull
    private BigDecimal limite;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CartaoStatus status;

    @NotNull
    @OneToOne
    private Proposta proposta;

    @OneToMany
    private Set<Biometria> biometrias = new HashSet<>();

    @OneToMany
    private Set<Bloqueio> bloqueios = new HashSet<>();

    @Deprecated
    public Cartao() {
    }

    public Cartao(@NotNull String numero, @NotNull LocalDateTime emitidoEm, @NotNull String titular, @NotNull BigDecimal limite) {
        Assert.isTrue(StringUtils.hasText(numero), "número não pode estar em branco");
        Assert.notNull(emitidoEm, "data de emissão não pode ser nulo");
        Assert.isTrue(StringUtils.hasText(titular), "titular não pode estar em branco");
        Assert.isTrue(limite.compareTo(BigDecimal.ZERO) > 0, "limite deve ser maior que 0");

        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.status = CartaoStatus.DESBLOQUEADO;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public CartaoStatus getStatus() {
        return status;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public Set<Biometria> getBiometrias() {
        return biometrias;
    }

    public Set<Bloqueio> getBloqueios() {
        return bloqueios;
    }

    public void associarProposta(Proposta proposta) {
        Assert.notNull(proposta, "a proposta não pode ser nula");
        this.proposta = proposta;
    }

    public void adicionaBiometria(Biometria biometria) {
        Assert.notNull(biometria, "biometria não pode ser nula");
        this.biometrias.add(biometria);
    }

    public boolean estaBloqueado() {
        return this.status == CartaoStatus.BLOQUEADO;
    }

    public void adicionarBloqueio(Bloqueio bloqueio) {
        Assert.notNull(bloqueio, "bloqueio não pode ser nulo");
        this.bloqueios.add(bloqueio);
        this.status = CartaoStatus.BLOQUEADO;
    }
}
