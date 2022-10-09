package ajedrez.cliente;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;

class Jugador implements IJugador {


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
	public EnumError registrarJugador(String nombre) {
        EnumError retorno = _controlador.registrarJugador(nombre);
        if (retorno == EnumError.SIN_ERROR) _nombre = nombre;
        
		return retorno;
	}


	/* Miembros privados. */

	private String _nombre;
	private EnumColorPieza _colorDePiezas;
	private ControladorCliente _controlador;

}
