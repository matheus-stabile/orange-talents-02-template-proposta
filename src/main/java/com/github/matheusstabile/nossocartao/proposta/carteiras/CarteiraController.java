package com.github.matheusstabile.nossocartao.proposta.carteiras;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.JwtDecoder;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class CarteiraController {

    private final EntityManager entityManager;
    private final CarteiraService carteiraService;
    private final Tracer tracer;

    @Autowired
    public CarteiraController(EntityManager entityManager, CarteiraService carteiraService, Tracer tracer) {
        this.entityManager = entityManager;
        this.carteiraService = carteiraService;
        this.tracer = tracer;
    }

    @PostMapping("/cartoes/{idCartao}/carteiras/paypal")
    public ResponseEntity<?> adicionarCarteiraPaypal(@PathVariable("idCartao") Long idCartao, @RequestBody @Valid CarteiraRequest carteiraRequest, @RequestHeader("Authorization") String token, UriComponentsBuilder uri) {
        tracer.activeSpan().setTag("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().setBaggageItem("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().log("Associação de carteira paypal");

        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cartão não encontrado");

        Cartao cartao = cartaoOptional.get();

        if (!cartao.pertenceAoUsuario(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "cartão não pertence ao usuário logado");

        if (cartao.temCarteira(TipoCarteira.PAYPAL))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "cartão já tem uma carteira paypal associada");

        return carteiraService.associaCarteira(cartao, TipoCarteira.PAYPAL, cartao.getProposta().getEmail(), uri);
    }

    @PostMapping("/cartoes/{idCartao}/carteiras/samsungpay")
    public ResponseEntity<?> adicionarCarteiraSamsungpay(@PathVariable("idCartao") Long idCartao, @RequestBody @Valid CarteiraRequest carteiraRequest, @RequestHeader("Authorization") String token, UriComponentsBuilder uri) {
        tracer.activeSpan().setTag("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().setBaggageItem("user.email", JwtDecoder.pegaEmail(token));
        tracer.activeSpan().log("Associação de carteira samsung pay");

        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cartão não encontrado");

        Cartao cartao = cartaoOptional.get();

        if (!cartao.pertenceAoUsuario(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "cartão não pertence ao usuário logado");

        if (cartao.temCarteira(TipoCarteira.SAMSUNG_PAY))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "cartão já tem uma carteira samsung pay associada");

        return carteiraService.associaCarteira(cartao, TipoCarteira.SAMSUNG_PAY, cartao.getProposta().getEmail(), uri);
    }
}
