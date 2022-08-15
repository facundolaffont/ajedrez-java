package ajedrez.cliente;

import ajedrez.modelo.EnumColorPieza;
import ajedrez.modelo.EnumError;

public interface IJugador {

    EnumError abandonarPartida();

    EnumColorPieza getColorDePiezas();

}
