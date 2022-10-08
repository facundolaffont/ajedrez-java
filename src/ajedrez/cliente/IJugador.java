package ajedrez.cliente;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;

public interface IJugador {

    EnumColorPieza getColorDePiezas();

    EnumError registrarJugador(String nombre);

}
