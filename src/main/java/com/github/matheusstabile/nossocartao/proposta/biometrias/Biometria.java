package com.github.matheusstabile.nossocartao.proposta.biometrias;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private byte[] digital;

    @NotNull
    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(@NotNull String digital, @NotNull Cartao cartao) {
        Assert.isTrue(StringUtils.hasText(digital), "digital não pode estar em branco");
        Assert.notNull(cartao, "cartão não pode ser nulo");

        this.digital = Base64.encodeBase64(digital.getBytes());
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Biometria biometria = (Biometria) o;
        return Arrays.equals(digital, biometria.digital);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(digital);
    }
}
