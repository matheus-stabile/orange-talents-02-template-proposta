package com.github.matheusstabile.nossocartao.proposta.bloqueios;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.JwtDecoder;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.InformacoesObrigatorias;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;

@RestController
public class BloqueioController {

    private final EntityManager entityManager;
    private final BloqueioService bloqueioService;
    private final Logger logger = LoggerFactory.getLogger(BloqueioController.class);
    private final Tracer tracer;

    @Autowired
    public BloqueioController(EntityManager entityManager, BloqueioService bloqueioService, Tracer tracer) {
        this.entityManager = entityManager;
        this.bloqueioService = bloqueioService;
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

        return bloqueioService.bloqueia(cartao, request);
    }
}
