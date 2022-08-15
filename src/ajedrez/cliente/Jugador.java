package ajedrez.cliente;

import ajedrez.modelo.EnumColorPieza;
import ajedrez.modelo.EnumError;

public class Jugador implements IJugador {


	/* Miembros públicos. */

	public Jugador(ControladorCliente controlador) {
		this.controlador = controlador;
		this.nombre = null;
		this.colorDePiezas = EnumColorPieza.SIN_COLOR;
    }

	public Jugador(ControladorCliente controlador, String nombre, EnumColorPieza colorDePiezas) {
		this.controlador = controlador;
		this.nombre = nombre;
		this.colorDePiezas = colorDePiezas;
	}
	
	/**
	 * {@return Devuelve el nombre del jugador, o {@code null},
	 * si todavía no tiene nombre asignado.}
	 */
	public String getNombre() {
		return nombre;
	}

	public EnumColorPieza getColorDePiezas() {
		return colorDePiezas;
	}

	public void setColorDePiezas(EnumColorPieza colorDePiezas) {
		this.colorDePiezas = colorDePiezas;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
    /**
	 * Solicita al servidor si puede registrarse el nombre de usuario.
	 *
	 * @return SIN_ERROR (se registró el jugador);
	 * 		JUGADOR_EXISTENTE (ya existe un jugador con ese nombre);
	 * 		SALA_LLENA (no hay lugar para nuevos jugadores).
	 */
	public EnumError solicitarRegistroDeJugador() {
		return controlador.registrarJugador(nombre);
	}
	
	public EnumError abandonarPartida() {
		return controlador.abandonarPartida(nombre);
	}

	public void cambiarColor() {
		colorDePiezas =
			colorDePiezas == EnumColorPieza.BLANCA
			? EnumColorPieza.NEGRA
			: EnumColorPieza.BLANCA;
	}


	/* Miembros privados. */

	private String nombre;
	private EnumColorPieza colorDePiezas;
	private ControladorCliente controlador;
}