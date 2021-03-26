package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.Crypto;
import com.github.matheusstabile.nossocartao.proposta.propostas.integracoes.AnaliseFinanceiraClient;
import com.github.matheusstabile.nossocartao.proposta.propostas.integracoes.AnaliseFinanceiraRequest;
import com.github.matheusstabile.nossocartao.proposta.propostas.integracoes.AnaliseFinanceiraResponse;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/propostas")
public class CadastrarPropostaController {

    private final PropostaRepository propostaRepository;
    private final AnaliseFinanceiraClient analiseFinanceiraClient;
    private final Tracer tracer;

    @Autowired
    public CadastrarPropostaController(PropostaRepository propostaRepository, AnaliseFinanceiraClient analiseFinanceiraClient, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.analiseFinanceiraClient = analiseFinanceiraClient;
        this.tracer = tracer;
    }

    @PostMapping
    @Transactional
    ResponseEntity cadastrarProposta(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uri) {
        tracer.activeSpan().setTag("user.email", propostaRequest.getEmail());
        tracer.activeSpan().setBaggageItem("user.email", propostaRequest.getEmail());
        tracer.activeSpan().log("Cadastro de nova proposta");

        if (propostaRepository.existsByDocumento(Crypto.encrypt(propostaRequest.getDocumento())))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "documento em uso");

        Proposta proposta = propostaRequest.toModel();
        propostaRepository.save(proposta);

        AnaliseFinanceiraResponse analiseFinanceiraResponse = analiseFinanceiraClient.analiza(new AnaliseFinanceiraRequest(proposta));
        proposta.atualizaStatusAnalise(analiseFinanceiraResponse.getResultadoSolicitacao().getPropostaStatus());
        proposta.encriptaDocumento();
        propostaRepository.save(proposta);

        if (proposta.getStatusProposta().equals(PropostaStatus.NAO_PROCESSADO)) {
            propostaRepository.delete(proposta);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problema de comunicação com serviço de análise financeira");
        }

        return ResponseEntity.created(uri.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri()).build();
    }
}
