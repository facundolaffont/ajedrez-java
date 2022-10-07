package ajedrez.cliente;

import ajedrez.controlador.ControladorCliente;
import ajedrez.modelo.EnumColorPieza;
import ajedrez.modelo.EnumError;

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
	
	public void cambiarColor() {
		_colorDePiezas =
			_colorDePiezas == EnumColorPieza.BLANCA
			? EnumColorPieza.NEGRA
			: EnumColorPieza.BLANCA;
	}
	
	@Override
	public EnumError setNombre(String nombre) {
		try {
			_nombre = nombre;
			return EnumError.SIN_ERROR;
		} catch (Exception e) { return EnumError.ERROR_DESCONOCIDO; }
	}

	@Override
	public EnumError registrarJugador() {
		if(_nombre == null) return EnumError.SIN_NOMBRE;
		return _controlador.registrarJugador(_nombre);
	}


	/* Miembros privados. */

	private String _nombre;
	private EnumColorPieza _colorDePiezas;
	private ControladorCliente _controlador;

}