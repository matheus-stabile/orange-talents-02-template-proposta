package com.github.matheusstabile.nossocartao.proposta.propostas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/propostas")
public class CadastrarPropostaController {

    private final PropostaRepository propostaRepository;
    private final Logger logger = LoggerFactory.getLogger(CadastrarPropostaController.class);

    @Autowired
    public CadastrarPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    ResponseEntity cadastrarProposta(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uri) {
        if (propostaRepository.existsByDocumento(propostaRequest.getDocumento())) {
            logger.error("[CRIAÇÃO DE PROPOSTA] Documento já está em uso");
            return ResponseEntity.unprocessableEntity().body(Map.of("documento", "já está em uso"));
        }

        Proposta proposta = propostaRequest.toModel();
        propostaRepository.save(proposta);
        logger.info("[CRIAÇÃO DE PROPOSTA] Nova proposta criada, id: {}", proposta.getId());

        return ResponseEntity.created(uri.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri()).build();
    }

}
