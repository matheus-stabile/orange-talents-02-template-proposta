package com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca;

import com.auth0.jwt.JWT;

public class JwtDecoder {

    public static String pegaEmail(String token) {
        return JWT.decode(token.substring(7))
                .getClaim("email")
                .asString();
    }
}
