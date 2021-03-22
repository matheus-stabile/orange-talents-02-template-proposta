package com.github.matheusstabile.nossocartao.proposta.carteiras;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
public class CarteiraController {

    private final EntityManager entityManager;
    private final CarteiraService carteiraService;

    @Autowired
    public CarteiraController(EntityManager entityManager, CarteiraService carteiraService) {
        this.entityManager = entityManager;
        this.carteiraService = carteiraService;
    }

    @PostMapping("/cartoes/{idCartao}/carteiras/paypal")
    public ResponseEntity<?> adicionarCarteiraPaypal(@PathVariable("idCartao") Long idCartao, @RequestBody @Valid CarteiraRequest carteiraRequest, UriComponentsBuilder uri) {
        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroPadronizado(Map.of("cartao", "não encontrado")));

        Cartao cartao = cartaoOptional.get();

        if (cartao.temCarteira(TipoCarteira.PAYPAL))
            return ResponseEntity.unprocessableEntity().body(new ErroPadronizado(Map.of("cartão", "já existe uma carteira Paypal associada a esse cartão")));

        return carteiraService.associaCarteira(cartao, TipoCarteira.PAYPAL, cartao.getProposta().getEmail(), uri);
    }

    @PostMapping("/cartoes/{idCartao}/carteiras/samsungpay")
    public ResponseEntity<?> adicionarCarteiraSamsungpay(@PathVariable("idCartao") Long idCartao, @RequestBody @Valid CarteiraRequest carteiraRequest, UriComponentsBuilder uri) {
        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroPadronizado(Map.of("cartao", "não encontrado")));

        Cartao cartao = cartaoOptional.get();

        if (cartao.temCarteira(TipoCarteira.SAMSUNG_PAY))
            return ResponseEntity.unprocessableEntity().body(new ErroPadronizado(Map.of("cartão", "já existe uma carteira Samsung Pay associada a esse cartão")));

        return carteiraService.associaCarteira(cartao, TipoCarteira.SAMSUNG_PAY, cartao.getProposta().getEmail(), uri);
    }
}
