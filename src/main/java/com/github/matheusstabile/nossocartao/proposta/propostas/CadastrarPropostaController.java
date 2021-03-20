package com.github.matheusstabile.nossocartao.proposta.propostas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

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
    ResponseEntity cadastrarProposta(@RequestBody @Valid PropostaRequest request) {

        Proposta proposta = request.toModel();
        propostaRepository.save(proposta);

        return ResponseEntity.created(fromCurrentRequest().path("/{id}").buildAndExpand(proposta.getId()).toUri()).build();
    }

}
