package com.damas.objetos;

/**
 * Representa uma Casa do tabuleiro.
 * Possui uma posi�ao (i,j) e pode conter uma Pe�a.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 */
public class Casa {

    private int x;
    private int y;
    private Pedra peca;

    public Casa(int x, int y) {
        this.x = x;
        this.y = y;
        this.peca = null;
    }

    public void colocarPeca(Pedra peca) {
        this.peca = peca;
    }

    public void removerPeca() {
        peca = null;
    }

    public Pedra getPeca() {
        return peca;
    }

    public boolean possuiPeca() {
        return peca != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
