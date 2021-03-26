package com.github.matheusstabile.nossocartao.proposta.bloqueios;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@RestController
public class BloqueioController {

    private final EntityManager entityManager;
    private final CartaoClient cartaoClient;
    private final Tracer tracer;

    @Autowired
    public BloqueioController(EntityManager entityManager, CartaoClient cartaoClient, Tracer tracer) {
        this.entityManager = entityManager;
        this.cartaoClient = cartaoClient;
        this.tracer = tracer;
    }

    @PostMapping("/cartoes/{idCartao}/bloquear")
    @Transactional
    public ResponseEntity<?> adicionarBloqueio(@PathVariable("idCartao") Long idCartao, @InformacoesObrigatorias HttpServletRequest request) {
        tracer.activeSpan().setTag("user.email", JwtDecoder.pegaEmail(request.getHeader("Authorization")));
        tracer.activeSpan().setBaggageItem("user.email", JwtDecoder.pegaEmail(request.getHeader("Authorization")));
        tracer.activeSpan().log("Cadastro de bloqueio de cartão");

        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cartão não encontrado");

        Cartao cartao = cartaoOptional.get();

        String token = request.getHeader("Authorization");

        if (!cartao.pertenceAoUsuario(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cartão não pertence ao usuário logado");

        if (cartao.estaBloqueado())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "cartão já está bloqueado");

        try {
            Map bloqueioRequest = Map.of("sistemaResponsavel", "${spring.application.name}");
            cartaoClient.bloqueiaCartao(cartao.getNumero(), bloqueioRequest);
            Bloqueio bloqueio = new Bloqueio(request.getRemoteAddr(), request.getHeader("User-Agent"), cartao);
            entityManager.persist(bloqueio);
            cartao.adicionarBloqueio(bloqueio);
            entityManager.merge(cartao);
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha de comunicação com a operadora do cartão");
        }

        return ResponseEntity.ok().build();
    }
}
