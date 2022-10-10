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

	/**
	 * Registra el nombre de un jugador en el servidor.
	 * 
	 * @return
	 *      SIN_CONEXION - Todavía no se estableció conexión;
	 * 		ERROR_DE_COMUNICACION - Error al intentar enviar el mensaje al servidor;
     *		PARTIDA_EN_CURSO - No se puede registrar al jugador porque hay una partida en curso;
     *		SIN_ERROR.
	 */
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
