package com.damas.objetos;

public final class PecaFactory {

    private PecaFactory() {
        // classe utilitária, não deve ser instanciada
    }

    public static Pedra criarPedra(Casa casa, Cor cor) {
        return new Pedra(casa, cor);
    }

    public static Dama criarDama(Casa casa, Cor cor) {
        return new Dama(casa, cor);
    }

    public static Dama promoverParaDama(Casa casa) {
        return criarDama(casa, casa.getPeca().getCor());
    }
}