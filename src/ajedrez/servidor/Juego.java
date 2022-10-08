package ajedrez.servidor;

import ajedrez.compartido.EnumEstadoDeJuego;

class Juego {

    /* Miembros públicos. */

    public Juego() {
        _estadoDelJuego = EnumEstadoDeJuego.SIN_PARTIDA;
    }

    public EnumEstadoDeJuego getEstadoDeJuego() {
        return _estadoDelJuego;
    }

    public void setEstadoDeJuego(EnumEstadoDeJuego estadoDelJuego) {
        _estadoDelJuego = estadoDelJuego;
    }


    /* Miembros privados. */

    private EnumEstadoDeJuego _estadoDelJuego;
}
