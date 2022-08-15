package ajedrez.modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ajedrez.servidor.ControladorServidor;

public class Juego {


	/* Miembros públicos. */
	
 	public Juego() {
		estadoDelJuego = EnumEstadoDeJuego.SIN_PARTIDA;
		tablero = new Tablero(this);
		jugadores = new ArrayList<JugadorRegistrado>();
		// controlador = Controlador();
	}

	public EnumEstadoDeJuego getEstadoDelJuego() {
		return estadoDelJuego;
	}
	
	public EnumError declararGanadorPorAbandono(String nombreDeJugadorQueAbandona) {
		switch(estadoDelJuego) {
			case SIN_PARTIDA:
			case PARTIDA_FINALIZADA_POR_ABANDONO:
            case PARTIDA_FINALIZADA_POR_JAQUE_MATE:
            case PARTIDA_FINALIZADA_POR_TABLA:
			// No se puede realizar el abandono de partida.
				return EnumError.ESTADO_DE_PARTIDA_INCORRECTO;
			default:
			// Se puede realizar el abandono de partida.
				estadoDelJuego = EnumEstadoDeJuego.PARTIDA_FINALIZADA_POR_ABANDONO;
				notificador.notificarAbandono(nombreDeJugadorQueAbandona);

				return EnumError.SIN_ERROR;
		}
	}
	
	/**
	 * Registra el jugador, si hay espacio en la sala y el nombre no está utilizado, ya.
	 * 
	 * Cuando se registra al jugador, se le asigna el color de las blancas,
	 * si es el primero en la sala; si no, el color de las negras.
	 * 
	 * @param nombreJugador
	 * @return
	 * 		SIN_ERROR: se registró el jugador.
	 * 		JUGADOR_EXISTENTE: ya existe un jugador con ese nombre.
	 * 		SALA_LLENA: no hay lugar para nuevos jugadores.
	 */
	EnumError registrarJugador(String nombreJugador) throws RemoteException {
		// Verifica si se puede registrar el usuario, y devuelve error,
		// en caso de que no se pueda.
		if(jugadores.size() == 2) return EnumError.SALA_LLENA;
		for (JugadorRegistrado jugador : jugadores)
			if(jugador.getNombre() == nombreJugador) return EnumError.JUGADOR_EXISTENTE;
		
		// Registra el jugador y le asigna un color de piezas.
		JugadorRegistrado jugador = new JugadorRegistrado(nombreJugador, 
			jugadores.size() == 0 ? EnumColorPieza.BLANCA : EnumColorPieza.NEGRA
		);
		jugadores.add(jugador);

		// Notifica a los observadores del cambio.
		notificador.notificarJugadorRegistrado(jugador.getNombre(), jugador.getColorDePiezas());

		return EnumError.SIN_ERROR;
	}


	/* Miembros privados. */

	@SuppressWarnings("unused") private Tablero tablero;
	private EnumEstadoDeJuego estadoDelJuego;
	private ControladorServidor notificador;
	private ArrayList<JugadorRegistrado> jugadores;

}