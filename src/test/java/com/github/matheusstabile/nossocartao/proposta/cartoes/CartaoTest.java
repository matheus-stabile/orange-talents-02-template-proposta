package com.github.matheusstabile.nossocartao.proposta.cartoes;

import com.github.matheusstabile.nossocartao.proposta.avisos.AvisoViagem;
import com.github.matheusstabile.nossocartao.proposta.biometrias.Biometria;
import com.github.matheusstabile.nossocartao.proposta.bloqueios.Bloqueio;
import com.github.matheusstabile.nossocartao.proposta.carteiras.Carteira;
import com.github.matheusstabile.nossocartao.proposta.carteiras.TipoCarteira;
import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartaoTest {

    Cartao cartaoValido;

    Proposta propostaValida;

    Biometria biometriaValida;

    Bloqueio bloqueioValido;

    AvisoViagem avisoViagemValido;

    Carteira carteiraValida;


    @BeforeEach
    void setup() {
        cartaoValido = new Cartao("numero", LocalDateTime.parse("2021-03-20T20:08:43.777489"), "titular", BigDecimal.ONE);
        propostaValida = new Proposta("documento", "email", "nome", "endereco", BigDecimal.ONE);
        biometriaValida = new Biometria("digital", cartaoValido);
        bloqueioValido = new Bloqueio("ip", "useragent", cartaoValido);
        avisoViagemValido = new AvisoViagem("ip", "useragent", cartaoValido);
        carteiraValida = new Carteira("email", TipoCarteira.PAYPAL, cartaoValido);

    }

    @Test
    @DisplayName("Não deve associar proposta ao cartão se a proposta for nula")
    void naoDeveAssociarPropostaNula() {

        assertThrows(IllegalArgumentException.class, () -> cartaoValido.associarProposta(null));
    }

    @Test
    @DisplayName("Deve associar proposta ao cartão se a proposta for valida")
    void deveAssociarPropostaSeForValida() {

        cartaoValido.associarProposta(propostaValida);

        assertEquals(propostaValida, cartaoValido.getProposta());
    }

    @Test
    @DisplayName("Não deve adicionar biometria se for nula")
    void naoDeveAdicionarBiometriaNula() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> cartaoValido.adicionaBiometria(null));
    }

    @Test
    @DisplayName("Deve adicionar biometria")
    void deveAdicionarBiometria() {

        cartaoValido.adicionaBiometria(biometriaValida);

        Assertions.assertTrue(cartaoValido.getBiometrias().contains(biometriaValida));
    }

    @Test
    @DisplayName("Não deve adicionar bloqueio se for nulo")
    void naoDeveAdicionarBloqueioNulo() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> cartaoValido.adicionarBloqueio(null));
    }

    @Test
    @DisplayName("Deve adicionar bloqueio")
    void deveAdicionarBloqueio() {

        cartaoValido.adicionarBloqueio(bloqueioValido);

        Assertions.assertAll(
                () -> Assertions.assertTrue(cartaoValido.getBloqueios().contains(bloqueioValido)),
                () -> Assertions.assertEquals(CartaoStatus.BLOQUEADO, cartaoValido.getStatus())
        );
    }

    @Test
    @DisplayName("Não deve adicionar aviso de viagem se for nulo")
    void naoDeveAdicionarAvisoDeViagemNulo() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> cartaoValido.adicionarAviso(null));
    }

    @Test
    @DisplayName("Deve adicionar aviso de viagem")
    void deveAdicionarAvisoDeViagem() {

        cartaoValido.adicionarAviso(avisoViagemValido);

        Assertions.assertTrue(cartaoValido.getAvisos().contains(avisoViagemValido));
    }

    @Test
    @DisplayName("Deve retornar erro se tentar verificar tipo da carteira informando um tipo de carteira nulo")
    void deveRetornarErroSeTentarVerificarTipoDaCarteiraComValorNulo() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> cartaoValido.temCarteira(null));
    }

    @Test
    @DisplayName("Deve verificar tipo da carteira informando um tipo de carteira válido")
    void deveVerificarTipoDaCarteiraInformandoUmTipoDeCarteiraValido() {
        cartaoValido.adicionaCarteira(carteiraValida);
        Assertions.assertTrue(cartaoValido.temCarteira(TipoCarteira.PAYPAL));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar adicionar uma carteira nula")
    void deveRetornarErroSeTentarAdicionarCarteiraNula() {

        assertThrows(IllegalArgumentException.class, () -> cartaoValido.adicionaCarteira(null));
    }

    @Test
    @DisplayName("Deve adicionar uma carteira válida")
    void deveAdicionarCarteiraValida() {
        cartaoValido.adicionaCarteira(carteiraValida);
        Assertions.assertTrue(cartaoValido.getCarteiras().contains(carteiraValida));
    }
}