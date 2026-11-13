package com.damas.objetos;


public enum Cor {
    BRANCA,
    VERMELHA;


    public Cor oposta() {
        return this == BRANCA ? VERMELHA : BRANCA;
    }

    public static Cor deTipo(int tipo) {
        switch (tipo) {
            case Peca.PEDRA_BRANCA:
            case Peca.DAMA_BRANCA:
                return BRANCA;
            case Peca.PEDRA_VERMELHA:
            case Peca.DAMA_VERMELHA:
                return VERMELHA;
            default:
                throw new IllegalArgumentException("Tipo de peça inválido: " + tipo);
        }
    }
}