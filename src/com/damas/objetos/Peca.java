package com.damas.objetos;

/**
 * Interface com os métodos abstratos das peças
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public interface Peca {

    public static final int PEDRA_BRANCA = 0;
    public static final int DAMA_BRANCA = 1;
    public static final int PEDRA_VERMELHA = 2;
    public static final int DAMA_VERMELHA = 3;

    public void mover(Casa destino);

    public boolean isMovimentoValido(Casa destino);

    public Cor getCor();

    public boolean isDama();

    public int getAlcance();

    public boolean isDeslocamentoPermitido(int sentidoY, int distanciaX, int distanciaY);

    public boolean exigeCaptura(int distancia);

    public boolean podeVirarDama();

    public default boolean mesmaCor(Peca outra) {
        return getCor() == outra.getCor();
    }

    public int getTipo();
}