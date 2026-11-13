package com.damas.objetos;

/**
 * O Tabuleiro do jogo.
 * Responsável por armazenar as 64 casas do jogo.
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 */

public class Tabuleiro {

    public static final int MAX_LINHAS = 8;
    public static final int MAX_COLUNAS = 8;

    public static final int X_ESQUERDA = -1;
    public static final int X_DIREITA = 1;
    public static final int Y_BAIXO = -1;
    public static final int Y_CIMA = 1;

    public static final int LINHAS_INICIAIS_POR_JOGADOR = 3;

    private Casa[][] casas;

    public Tabuleiro() {
        montarTabuleiro();
    }

    private void montarTabuleiro() {
        casas = new Casa[MAX_LINHAS][MAX_COLUNAS];
        for (int x = 0; x < MAX_LINHAS; x++) {
            for (int y = 0; y < MAX_COLUNAS; y++) {
                Casa casa = new Casa(x, y);
                casas[x][y] = casa;
            }
        }
    }

    public void posicionarPecasIniciais() {
        posicionarPedras(Cor.BRANCA, 0, LINHAS_INICIAIS_POR_JOGADOR);
        posicionarPedras(Cor.VERMELHA, MAX_COLUNAS - LINHAS_INICIAIS_POR_JOGADOR, MAX_COLUNAS);
    }


    private void posicionarPedras(Cor cor, int yInicial, int yFinal) {
        for (int x = 0; x < MAX_LINHAS; x++) {
            for (int y = yInicial; y < yFinal; y++) {
                if ((x + y) % 2 == 0) {
                    PecaFactory.criarPedra(casas[x][y], cor);
                }
            }
        }
    }

    public Casa getCasa(int x, int y) {
        return casas[x][y];
    }

    public boolean contem(int x, int y) {
        return x >= 0 && x < MAX_LINHAS && y >= 0 && y < MAX_COLUNAS;
    }
}