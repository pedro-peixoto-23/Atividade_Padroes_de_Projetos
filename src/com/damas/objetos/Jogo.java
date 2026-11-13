package com.damas.objetos;

import java.util.ArrayList;

/**
 * Controla o andamento de uma partida: de quem é a vez, a pontuação, a sequência de
 * capturas e o fim de jogo. Coordena o {@link Tabuleiro}, as peças e os jogadores.
 *
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */
public class Jogo {

    /** Quantidade de peças de cada jogador no início da partida. */
    public static final int PECAS_POR_JOGADOR = 12;
    /** Pontos (peças capturadas) necessários para vencer. */
    public static final int PONTOS_PARA_VENCER = PECAS_POR_JOGADOR;

    private Tabuleiro tabuleiro;
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private Cor corDaVez;
    private int jogadas;
    private int jogadasSemComerPeca;
    private ArrayList<Casa> pecasAComer;
    private Casa casaBloqueadaOrigem;

    public Jogo() {
        tabuleiro = new Tabuleiro();
        pecasAComer = new ArrayList<Casa>();
        jogadorUm = new Jogador("player branco");
        jogadorDois = new Jogador("player vermelho");

        corDaVez = Cor.BRANCA; // as brancas começam
        jogadas = 0;
        jogadasSemComerPeca = 0;
        casaBloqueadaOrigem = null;

        tabuleiro.posicionarPecasIniciais();
    }


    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);
        Pedra peca = origem.getPeca();

        if (peca == null) return;
        if (!podeMoverAPeca(origem, peca)) return;
        if (!peca.isMovimentoValido(destino)) return;
        if (!simularMovimentoEValidar(origem, destino)) return;

        // DURANTE UMA SEQUÊNCIA DE CAPTURAS, SÓ VALE UM MOVIMENTO QUE CAPTURE
        boolean capturou = !pecasAComer.isEmpty();
        if (casaBloqueadaOrigem != null && !capturou) return;

        executarMovimento(peca, destino, capturou);
    }

    private boolean podeMoverAPeca(Casa origem, Pedra peca) {
        if (peca.getCor() != corDaVez) return false;
        return casaBloqueadaOrigem == null || origem.equals(casaBloqueadaOrigem);
    }

    private void executarMovimento(Pedra peca, Casa destino, boolean capturou) {
        casaBloqueadaOrigem = null;
        peca.mover(destino);

        if (capturou) {
            comerPecas();
            if (deveContinuarJogando(destino)) {
                casaBloqueadaOrigem = destino;
            } else {
                trocarDeVez();
            }
        } else {
            jogadasSemComerPeca++;
            trocarDeVez();
        }

        jogadas++;
        if (destino.getPeca().podeVirarDama()) PecaFactory.promoverParaDama(destino);
    }

    private boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        Pedra peca = origem.getPeca();
        int casasComPecaSeguidas = 0;

        if (destino.getPeca() != null) return false;

        // SENTIDO DO MOVIMENTO E DISTÂNCIA DO MOVIMENTO
        int sentidoX = (destino.getX() - origem.getX());
        int sentidoY = (destino.getY() - origem.getY());
        int distanciaX = Math.abs(sentidoX);
        int distanciaY = Math.abs(sentidoY);

        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        sentidoX = sentidoX/distanciaX;
        sentidoY = sentidoY/distanciaY;

        // A PRÓPRIA PEÇA DIZ SE ACEITA ESSE DESLOCAMENTO (PEDRA: 1 CASA PARA FRENTE OU SALTO DE 2; DAMA: QUALQUER)
        if (!peca.isDeslocamentoPermitido(sentidoY, distanciaX, distanciaY)) return false;

        // SE A PEÇA SÓ PODE SE DESLOCAR ASSIM CAPTURANDO (SALTO DA PEDRA), TEM QUE HAVER PEÇA NO MEIO
        if (peca.exigeCaptura(distanciaX)) {
            Casa casa = tabuleiro.getCasa((destino.getX() - sentidoX), (destino.getY() - sentidoY));
            if (casa.getPeca() == null) return false;
        }

        //PERCORRER AS CASAS E VERIFICAR:
        // 1 - SE HÁ MAIS DE UMA PEÇA SEGUIDA NO CAMINHO (VERDADEIRO RETORNA FALSO)
        // 2 - SE HÁ UMA PEÇA NO CAMINHO E É DA MESMA COR (VERDADEIRO RETORNA FALSO)
        int i = origem.getX();
        int j = origem.getY();

        while (!((i == destino.getX()) || (j == destino.getY()))) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = tabuleiro.getCasa(i, j);
            Pedra pecaAlvo = alvo.getPeca();

            if (!(pecaAlvo == null)) {
                casasComPecaSeguidas += 1;

                // VE SE TEM UMA PECA DA MESMA COR NO CAMINHO, CASO TENHA, RETORNA FALSE
                if (peca.mesmaCor(pecaAlvo)) {
                    if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                    return false;
                }

            } else {

                // VE SE HÁ PEÇA PARA COMER NO CAMINHO E PASSAR A CASA À COLEÇÃO pecasAComer() PARA DEPOIS COME-LAS
                if (casasComPecaSeguidas == 1) {
                    Casa casa = tabuleiro.getCasa((alvo.getX() - sentidoX), (alvo.getY() - sentidoY));
                    pecasAComer.add(casa);
                }
                casasComPecaSeguidas = 0;
            }

            if (casasComPecaSeguidas == 2) {
                if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                return false;
            }
        }
        return true;
    }

    private boolean percorrerEVerificar(Casa origem, int deltaX, int deltaY) {

        Pedra peca = origem.getPeca();
        int x = origem.getX();
        int y = origem.getY();

        // PROCURA A PRIMEIRA PEÇA NO CAMINHO, ATÉ O ALCANCE DA PEÇA (PEDRA: SÓ A CASA VIZINHA; DAMA: A DIAGONAL TODA)
        for (int passo = 1; passo <= peca.getAlcance(); passo++) {
            x += deltaX;
            y += deltaY;

            if (!tabuleiro.contem(x, y)) return false;

            Pedra pecaAtual = tabuleiro.getCasa(x, y).getPeca();
            if (pecaAtual == null) continue;

            // PEÇA DA MESMA COR NO CAMINHO: NÃO DÁ PARA COMER
            if (peca.mesmaCor(pecaAtual)) return false;

            // PEÇA ADVERSÁRIA: SÓ DÁ PARA COMER SE A CASA ATRÁS DELA EXISTE E ESTÁ LIVRE
            int atrasX = x + deltaX;
            int atrasY = y + deltaY;
            return tabuleiro.contem(atrasX, atrasY) && !tabuleiro.getCasa(atrasX, atrasY).possuiPeca();
        }

        return false;
    }

    private boolean deveContinuarJogando(Casa origem) {

        if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_CIMA)) {
            return true;
        } else {

            if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA)) {
                return true;
            } else {

                if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_BAIXO)) {
                    return true;
                } else {

                    if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_BAIXO)) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    private void comerPecas() {
        int pecasComidas = pecasAComer.size();

        getJogadorDaVez().addPonto(pecasComidas);

        for (Casa casa : pecasAComer) {
            casa.removerPeca();
        }

        pecasAComer.removeAll(pecasAComer);

        jogadasSemComerPeca = 0;
    }

    private Jogador getJogadorDaVez() {
        return (corDaVez == Cor.BRANCA) ? jogadorUm : jogadorDois;
    }

    public void trocarDeVez() {
        corDaVez = corDaVez.oposta();
    }

    public int getGanhador() {
        if (jogadorUm.getPontos() >= PONTOS_PARA_VENCER) return 1;
        if (jogadorDois.getPontos() >= PONTOS_PARA_VENCER) return 2;
        return 0;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void setJogadorUm(Jogador jogador) {
        jogadorUm = jogador;
    }

    public void setJogadorDois(Jogador jogador) {
        jogadorDois = jogador;
    }

    public Jogador getJogadorUm() {
        return jogadorUm;
    }

    public Jogador getJogadorDois() {
        return jogadorDois;
    }

    public int getVez() {
        return (corDaVez == Cor.BRANCA) ? 1 : 2;
    }

    public int getJogadasSemComerPecas() {
        return jogadasSemComerPeca;
    }

    public int getJogada() {
        return jogadas;
    }

    public Casa getCasaBloqueada() {
        return casaBloqueadaOrigem;
    }

    @Override
    public String toString() {

        String retorno = "Vez: " + getJogadorDaVez().getNome() + "\n";

        retorno += "Nº de jogadas: " + getJogada() + "\n";
        retorno += "Jogadas sem comer peça: " + getJogadasSemComerPecas() + "\n";
        retorno += "\n";
        retorno += "Informações do(a) jogador(a) " + jogadorUm.getNome() + "\n";
        retorno += "Pontos: " + jogadorUm.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (PECAS_POR_JOGADOR - jogadorDois.getPontos()) + "\n";
        retorno += "\n";
        retorno += "Informações do(a) jogador(a) " + jogadorDois.getNome() + "\n";
        retorno += "Pontos: " + jogadorDois.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (PECAS_POR_JOGADOR - jogadorUm.getPontos()) + "\n";

        if (casaBloqueadaOrigem != null) {
            retorno += "\n";
            retorno += "Mova a peça na casa " + casaBloqueadaOrigem.getX() + ":" + casaBloqueadaOrigem.getY() + "!";
        }

        return retorno;
    }
}