package com.damas.objetos;

/**
 * Representa uma Peça do jogo.
 * Possui uma casa e um tipo associado.
 *
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */
public class Pedra implements Peca {

    protected Casa casa;
    protected final Cor cor;

    public Pedra(Casa casa, Cor cor) {
        this.casa = casa;
        this.cor = cor;
        casa.colocarPeca(this);
    }

    public Pedra(Casa casa, int tipo) {
        this(casa, Cor.deTipo(tipo));
    }

    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {

        // SENTIDO UNITÁRIO E DISTANCIA X E Y DA CASA ATUAL ATÉ A CASA DE DESTINO
        int distanciaX = Math.abs(destino.getX() - casa.getX());
        int distanciaY = Math.abs(destino.getY() - casa.getY());

        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        // REGRA DE MOVIMENTO NO CASO DA DISTÂNCIA SER DE 2 CASAS (MOVIMENTO DE COMER PEÇA)
        if ((distanciaX <= 2 || distanciaY <= 2) && (distanciaX == distanciaY)) {
            return true;
        }

        return false;
    }

    public int getSentidoFrente() {
        return (cor == Cor.BRANCA) ? Tabuleiro.Y_CIMA : Tabuleiro.Y_BAIXO;
    }

    @Override
    public int getAlcance() {
        return 1;
    }

    @Override
    public boolean isDeslocamentoPermitido(int sentidoY, int distanciaX, int distanciaY) {
        if (distanciaX != distanciaY) return false;
        if (distanciaX == 1) return sentidoY == getSentidoFrente();
        return distanciaX == 2;
    }

    @Override
    public boolean exigeCaptura(int distancia) {
        return distancia == 2;
    }

    @Override
    public boolean podeVirarDama() {
        int linhaDePromocao = (cor == Cor.BRANCA) ? Tabuleiro.MAX_LINHAS - 1 : 0;
        return casa.getY() == linhaDePromocao;
    }

    @Override
    public Cor getCor() {
        return cor;
    }

    @Override
    public boolean isDama() {
        return false;
    }

    @Override
    public int getTipo() {
        if (cor == Cor.BRANCA) {
            return isDama() ? DAMA_BRANCA : PEDRA_BRANCA;
        }
        return isDama() ? DAMA_VERMELHA : PEDRA_VERMELHA;
    }
}