package com.github.matheusstabile.nossocartao.proposta.propostas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    List<Proposta> findByPropostaStatusIs(PropostaStatus naoProcessado);

    List<Proposta> findByPropostaStatusIsAndCartaoIdIsNull(PropostaStatus elegivel);
}
