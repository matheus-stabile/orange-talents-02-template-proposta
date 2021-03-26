package com.github.matheusstabile.nossocartao.proposta.biometrias;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.JwtDecoder;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class BiometriaController {

    private final EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(BiometriaController.class);
    private final Tracer tracer;

    public BiometriaController(EntityManager entityManager, Tracer tracer) {
        this.entityManager = entityManager;
        this.tracer = tracer;
    }

    @PostMapping("/cartoes/{idCartao}/biometria")
    @Transactional
    public ResponseEntity<?> adicionaBiometria(@PathVariable("idCartao") Long idCartao, @RequestBody @Valid BiometriaRequest biometriaRequest, @RequestHeader(name = "Authorization") String token, UriComponentsBuilder uri) {
        tracer.activeSpan().setTag("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().setBaggageItem("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().log("Cadastro de biometria");

        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cartão não encontrado");

        Cartao cartao = cartaoOptional.get();

        if (!cartao.pertenceAoUsuario(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cartão não pertence ao usuário logado");

        Biometria biometria = biometriaRequest.toModel(cartaoOptional.get());
        entityManager.persist(biometria);

        cartao.adicionaBiometria(biometria);
        entityManager.merge(cartao);

        return ResponseEntity.created(uri.path("/cartoes/{idCartao}/biometria/{id}").buildAndExpand(idCartao, biometria.getId()).toUri()).build();
    }
}
