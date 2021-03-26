package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.JwtDecoder;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class ConsultarPropostaController {

    private final EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(ConsultarPropostaController.class);
    private final Tracer tracer;

    @Autowired
    public ConsultarPropostaController(EntityManager entityManager, Tracer tracer) {
        this.entityManager = entityManager;
        this.tracer = tracer;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarPropostaPeloId(@PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        tracer.activeSpan().setTag("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().setBaggageItem("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().log("Consulta de propostas");

        Optional<Proposta> propostaOptional = Optional.ofNullable(entityManager.find(Proposta.class, id));

        if (propostaOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "proposta não cadastrada");

        Proposta proposta = propostaOptional.get();

        if (!proposta.pertenceAoUsuario(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "proposta não pertence ao usuário logado");

        return ResponseEntity.ok(new PropostaResponse(proposta));
    }
}
