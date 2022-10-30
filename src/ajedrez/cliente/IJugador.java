package ajedrez.cliente;

import ajedrez.compartido.EnumColorPieza;

public interface IJugador {

    EnumColorPieza getColorDePiezas();

    int registrarJugador(String nombre);

}
