package com.github.matheusstabile.nossocartao.proposta.avisos;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.JwtDecoder;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.InformacoesObrigatorias;
import feign.FeignException;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
public class AvisoViagemController {

    private final EntityManager entityManager;
    private final CartaoClient cartaoClient;
    private final Tracer tracer;

    @Autowired
    public AvisoViagemController(EntityManager entityManager, CartaoClient cartaoClient, Tracer tracer) {
        this.entityManager = entityManager;
        this.cartaoClient = cartaoClient;
        this.tracer = tracer;
    }

    @PostMapping("/cartoes/{idCartao}/viagem")
    @Transactional
    public ResponseEntity<?> avisarViagem(@PathVariable("idCartao") Long idCartao, @RequestBody @Valid AvisoViagemRequest avisoViagemRequest, @InformacoesObrigatorias HttpServletRequest request) {
        tracer.activeSpan().setTag("user.email", JwtDecoder.pegaEmail(request.getHeader("Authorization")));
        tracer.activeSpan().setBaggageItem("user.email", JwtDecoder.pegaEmail(request.getHeader("Authorization")));
        tracer.activeSpan().log("Cadastro de aviso de viagem");

        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cartão não encontrado");

        Cartao cartao = cartaoOptional.get();

        String token = request.getHeader("Authorization");

        if (!cartao.pertenceAoUsuario(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cartão não pertence ao usuário logado");

        try {
            ResponseEntity respostaOperadora = cartaoClient.informaViagem(cartao.getNumero(), avisoViagemRequest);
            AvisoViagem avisoViagem = new AvisoViagem(request.getRemoteAddr(), request.getHeader("user-Agent"), cartao);
            entityManager.persist(avisoViagem);
            cartao.adicionarAviso(avisoViagem);
            entityManager.merge(cartao);
        }catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha de comunicação com a operadora do cartão");
        }

        return ResponseEntity.ok().build();
    }
}
