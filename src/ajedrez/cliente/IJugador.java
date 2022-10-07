package ajedrez.cliente;

import ajedrez.modelo.EnumColorPieza;
import ajedrez.modelo.EnumError;

public interface IJugador {

    EnumColorPieza getColorDePiezas();

    EnumError setNombre(String nombre);

    EnumError registrarJugador();
}
