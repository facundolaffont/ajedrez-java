package ajedrez.modelo;

import java.util.ArrayList;

public class Juego {


	/* Miembros públicos. */
	
 	public Juego() {
		_estadoDelJuego = EnumEstadoDeJuego.SIN_PARTIDA;
		_tablero = new Tablero(this);
		_jugadores = new ArrayList<JugadorRegistrado>();
		// controlador = Controlador();
	}

	public EnumEstadoDeJuego getEstadoDelJuego() {
		return _estadoDelJuego;
	}
	

	/* Miembros privados. */

	@SuppressWarnings("unused") private Tablero _tablero;
	@SuppressWarnings("unused") private ArrayList<JugadorRegistrado> _jugadores;
	private EnumEstadoDeJuego _estadoDelJuego;

}