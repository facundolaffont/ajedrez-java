package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;

class JugadorRegistrado {

	/* Miembros públicos. */

	public JugadorRegistrado() {
		_nombre = null;
		_colorDePiezas = EnumColorPieza.SIN_COLOR;
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

	public void setNombre(String nombre) {
		_nombre = nombre;
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


	/* Miembros privados. */

	private String _nombre;
	private EnumColorPieza _colorDePiezas;
}