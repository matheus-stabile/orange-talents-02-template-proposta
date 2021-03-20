package com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions;

import java.util.Map;

public class ErroPadronizado {

    private final Map<String, String> erros;

    public ErroPadronizado(Map<String, String> erros) {
        this.erros = erros;
    }

    public Map<String, String> getErros() {
        return erros;
    }
}
