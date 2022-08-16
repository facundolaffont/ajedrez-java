package ajedrez.cliente;

import ajedrez.controlador.ControladorCliente;
import ajedrez.modelo.EnumColorPieza;

public class Jugador implements IJugador {


	/* Miembros públicos. */

	public Jugador(ControladorCliente controlador) {
		_controlador = controlador;
		_nombre = null;
		_colorDePiezas = EnumColorPieza.SIN_COLOR;
    }

	public Jugador(ControladorCliente controlador, String nombre, EnumColorPieza colorDePiezas) {
		_controlador = controlador;
		_nombre = nombre;
		_colorDePiezas = colorDePiezas;
	}
	
	/**
	 * {@return Devuelve el nombre del jugador, o {@code null},
	 * si todavía no tiene nombre asignado.}
	 */
	public String getNombre() {
		return _nombre;
	}

	public EnumColorPieza getColorDePiezas() {
		return _colorDePiezas;
	}

	public void setColorDePiezas(EnumColorPieza colorDePiezas) {
		_colorDePiezas = colorDePiezas;
	}
	
	public void setNombre(String nombre) {
		_nombre = nombre;
	}
	
	public void cambiarColor() {
		_colorDePiezas =
			_colorDePiezas == EnumColorPieza.BLANCA
			? EnumColorPieza.NEGRA
			: EnumColorPieza.BLANCA;
	}


	/* Miembros privados. */

	private String _nombre;
	private EnumColorPieza _colorDePiezas;
	private ControladorCliente _controlador;

}