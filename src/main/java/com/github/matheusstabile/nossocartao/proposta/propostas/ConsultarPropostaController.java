package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class ConsultarPropostaController {

    private final PropostaRepository propostaRepository;
    private final Logger logger = LoggerFactory.getLogger(ConsultarPropostaController.class);

    @Autowired
    public ConsultarPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarPropostaPeloId(@PathVariable("id") Long id) {
        Optional<Proposta> proposta = propostaRepository.findById(id);

        if (proposta.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroPadronizado(Map.of("proposta", "não encontrada")));

        return ResponseEntity.ok(new PropostaResponse(proposta.get()));
    }
}
