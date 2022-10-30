package ajedrez.cliente;

import ajedrez.compartido.EnumColorPieza;

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

	/**
	 * Registra el nombre de un jugador en el servidor.
	 * 
	 * @return
     *		0 - Jugador registrado;
	 *      -1 - Todavía no se estableció conexión con el servidor;
	 * 		-2 - Error al intentar enviar el mensaje al servidor;
     *		-3 - Hay una partida en curso;
	 *		-4 - No existe conexión con el socket especificado.
	 */
	@Override
	public int registrarJugador(String nombre) {
		int codigoError = _controlador.registrarJugador(nombre);
		
		switch(codigoError) {
			case -1: return -1;
			case -2: return -2;
			case -3: return -3;
			case -4: return -4;
		}

		_nombre = nombre;
		return 0;
	}


	/* Miembros privados. */

	private String _nombre;
	private EnumColorPieza _colorDePiezas;
	private ControladorCliente _controlador;
}
