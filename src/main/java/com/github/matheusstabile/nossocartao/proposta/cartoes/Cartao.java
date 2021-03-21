package com.github.matheusstabile.nossocartao.proposta.cartoes;

import com.github.matheusstabile.nossocartao.proposta.biometrias.Biometria;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.AnaliseCartaoResponse;
import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;
import org.springframework.util.Assert;

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
    @OneToOne
    private Proposta proposta;

    @OneToMany
    private Set<Biometria> biometrias = new HashSet<>();

    @Deprecated
    public Cartao() {
    }

    public Cartao(@NotNull String numero, @NotNull LocalDateTime emitidoEm, @NotNull String titular, @NotNull BigDecimal limite) {
        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
    }

    public Long getId() {
        return id;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public Set<Biometria> getBiometrias() {
        return biometrias;
    }

    public void associarProposta(Proposta proposta) {
        Assert.notNull(proposta, "a proposta não pode ser nula");
        this.proposta = proposta;
    }

    public void adicionaBiometria(Biometria biometria) {
        Assert.notNull(biometria, "biometria não pode ser nula");
        this.biometrias.add(biometria);
    }
}
