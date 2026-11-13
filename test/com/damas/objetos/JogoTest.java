package com.damas.objetos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JogoTest {

    @Test
    void deveMoverPedraBrancaUmaCasaNaDiagonal() {
        Jogo jogo = new Jogo();

        jogo.moverPeca(0, 2, 1, 3);

        assertNull(jogo.getTabuleiro().getCasa(0, 2).getPeca());
        assertNotNull(jogo.getTabuleiro().getCasa(1, 3).getPeca());

        assertEquals(Peca.PEDRA_BRANCA,
                jogo.getTabuleiro().getCasa(1, 3).getPeca().getTipo());

        assertEquals(2, jogo.getVez());
    }

    @Test
    void naoDeveMoverPedraBrancaParaTras() {
        Jogo jogo = new Jogo();

        // Branca: (0, 2) -> (1, 3)
        jogo.moverPeca(0, 2, 1, 3);

        // Vermelha: (3, 5) -> (2, 4)
        jogo.moverPeca(3, 5, 2, 4);

        // Branca tenta voltar: (1, 3) -> (0, 2)
        jogo.moverPeca(1, 3, 0, 2);

        assertNull(jogo.getTabuleiro().getCasa(0, 2).getPeca());
        assertNotNull(jogo.getTabuleiro().getCasa(1, 3).getPeca());

        assertEquals(Peca.PEDRA_BRANCA,
                jogo.getTabuleiro().getCasa(1, 3).getPeca().getTipo());

        assertEquals(1, jogo.getVez());
    }

    @Test
    void devePermitirQueDamaSeMovimenteVariasCasasNaDiagonal() {
        Jogo jogo = new Jogo();

        Casa origem = jogo.getTabuleiro().getCasa(2, 2);
        Casa destino = jogo.getTabuleiro().getCasa(4, 4);

        new Dama(origem, Peca.DAMA_BRANCA);

        jogo.moverPeca(2, 2, 4, 4);

        assertNull(origem.getPeca());
        assertNotNull(destino.getPeca());

        assertEquals(Peca.DAMA_BRANCA, destino.getPeca().getTipo());
    }

    @Test
    void naoDeveMoverPecaParaCasaOcupada() {
        Jogo jogo = new Jogo();

        Casa origem = jogo.getTabuleiro().getCasa(2, 2);
        Casa destino = jogo.getTabuleiro().getCasa(3, 3);

        new Pedra(destino, Peca.PEDRA_VERMELHA);

        jogo.moverPeca(2, 2, 3, 3);

        assertNotNull(origem.getPeca());
        assertNotNull(destino.getPeca());

        assertEquals(Peca.PEDRA_BRANCA, origem.getPeca().getTipo());
        assertEquals(Peca.PEDRA_VERMELHA, destino.getPeca().getTipo());

        assertEquals(1, jogo.getVez());
    }

    @Test
    void devePermitirQuePedraBrancaCapturePedraVermelha() {
        Jogo jogo = new Jogo();

        Casa origem = jogo.getTabuleiro().getCasa(2, 2);
        Casa pecaAdversaria = jogo.getTabuleiro().getCasa(3, 3);
        Casa destino = jogo.getTabuleiro().getCasa(4, 4);

        new Pedra(pecaAdversaria, Peca.PEDRA_VERMELHA);

        jogo.moverPeca(2, 2, 4, 4);

        assertNull(origem.getPeca());
        assertNull(pecaAdversaria.getPeca());
        assertNotNull(destino.getPeca());

        assertEquals(Peca.PEDRA_BRANCA, destino.getPeca().getTipo());
    }

    @Test
    void deveRemoverPecaAdversariaAposCaptura() {
        Jogo jogo = new Jogo();

        Casa pecaVermelha = jogo.getTabuleiro().getCasa(3, 3);

        new Pedra(pecaVermelha, Peca.PEDRA_VERMELHA);

        jogo.moverPeca(2, 2, 4, 4);

        assertNull(pecaVermelha.getPeca());
    }

    @Test
    void deveAumentarPontuacaoAposCaptura() {
        Jogo jogo = new Jogo();

        Casa pecaVermelha = jogo.getTabuleiro().getCasa(3, 3);

        new Pedra(pecaVermelha, Peca.PEDRA_VERMELHA);

        assertEquals(0, jogo.getJogadorUm().getPontos());

        jogo.moverPeca(2, 2, 4, 4);

        assertEquals(1, jogo.getJogadorUm().getPontos());
    }

    @Test
    void devePromoverPedraBrancaParaDama() {
        Jogo jogo = new Jogo();

        // 1. Branca: (0,2) -> (1,3)
        jogo.moverPeca(0, 2, 1, 3);

        // 2. Vermelha: (3,5) -> (2,4)
        jogo.moverPeca(3, 5, 2, 4);

        // 3. Branca: (1,3) -> (3,5), captura
        jogo.moverPeca(1, 3, 3, 5);

        // 4. Vermelha: (2,6) -> (4,4), captura
        jogo.moverPeca(2, 6, 4, 4);

        // 5. Branca: (4,2) -> (5,3)
        jogo.moverPeca(4, 2, 5, 3);

        // 6. Vermelha: (1,7) -> (2,6)
        jogo.moverPeca(1, 7, 2, 6);

        // 7. Branca: (5,3) -> (3,5), captura
        jogo.moverPeca(5, 3, 3, 5);

        // 8. Branca: (3,5) -> (1,7), captura e chega à última linha
        jogo.moverPeca(3, 5, 1, 7);

        assertNull(jogo.getTabuleiro().getCasa(3, 5).getPeca());
        assertNotNull(jogo.getTabuleiro().getCasa(1, 7).getPeca());

        assertEquals(
                Peca.DAMA_BRANCA,
                jogo.getTabuleiro().getCasa(1, 7).getPeca().getTipo()
        );
    }
}