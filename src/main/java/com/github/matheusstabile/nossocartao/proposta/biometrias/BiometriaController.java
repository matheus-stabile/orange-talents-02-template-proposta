package com.github.matheusstabile.nossocartao.proposta.biometrias;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
public class BiometriaController {

    private final EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(BiometriaController.class);

    public BiometriaController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/cartoes/{id}/biometria")
    @Transactional
    public ResponseEntity<?> adicionaBiometria(@PathVariable("id") Long id, @RequestBody @Valid BiometriaRequest biometriaRequest, UriComponentsBuilder uri) {
        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, id));

        if (cartaoOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroPadronizado(Map.of("cartao", "não encontrado")));

        Biometria biometria = biometriaRequest.toModel();
        entityManager.persist(biometria);

        Cartao cartao = cartaoOptional.get();
        cartao.adicionaBiometria(biometria);
        entityManager.merge(cartao);

        return ResponseEntity.created(uri.path("/{id}").buildAndExpand(biometria.getId()).toUri()).build();
    }
}
