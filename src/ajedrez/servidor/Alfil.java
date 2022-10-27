package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumEstadoDeJuego;
import ajedrez.compartido.EnumTipoPieza;

class Alfil extends Pieza {
	
	/* Miembros públicos. */
	
	public Alfil(EnumColorPieza color) {
		super();
		this.color = color;
	}
	
	@Override
	public EnumError moverA(char letraFinal, int numeroFinal, EnumEstadoDeJuego estadoDelJuego, boolean realizarMovimiento, boolean calcularJaque) {
		EnumError retorno;
		boolean movimientoPosible, hayPieza, hayCaptura, quedariaEnJaque;
		char letraInicial;
		int numeroInicial, cantidadCasillas, offsetDeLetra, offsetDeNumero,
		indiceDeCasilla;
		Pieza piezaEnCasillaSeleccionada;
		
		retorno = EnumError.SIN_ERROR; // Para evitar error de sintaxis.
		piezaEnCasillaSeleccionada = null; // Para evitar error de sintaxis.
		hayCaptura = false;
		letraInicial = tablero.consultarPosicionLetra(this);
		numeroInicial = tablero.consultarPosicionNumero(this);
		movimientoPosible = false;
		if(
			Math.abs(letraFinal - letraInicial)
			!=
			Math.abs(numeroFinal - numeroInicial)
		)
		// La casilla de destino no está en diagonal.
			retorno = EnumError.CASILLA_INVALIDA;
		else {
		// La casilla de destino está en diagonal.
			// Guarda en una variable la cantidad de casillas
			// a recorrer.
			cantidadCasillas = Math.abs(letraFinal - letraInicial);
			
			// Chequeamos si hay alguna pieza entre la casilla
			// seleccionada y la casilla en la que se encuentra el alfil.
			if(numeroFinal > numeroInicial) offsetDeNumero = +1;
			else offsetDeNumero = -1;
			if(letraFinal > letraInicial) offsetDeLetra = +1;
			else offsetDeLetra = -1;			
			hayPieza = false;
			indiceDeCasilla = 1;
			while(!hayPieza && indiceDeCasilla < cantidadCasillas) {
				if(tablero.consultarPieza(
					(char) (letraInicial + (offsetDeLetra * indiceDeCasilla)),
					numeroInicial + (offsetDeNumero * indiceDeCasilla)
				) != null)
					hayPieza = true;
				indiceDeCasilla++;
			}
			
			if(hayPieza)
			// Hay una pieza entre la casilla seleccionada y el alfil.
				retorno = EnumError.SALTO_NO_PERMITIDO;
			else {
				piezaEnCasillaSeleccionada = tablero.consultarPieza(letraFinal, numeroFinal);
				if(piezaEnCasillaSeleccionada == null)
				// No hay pieza en la casilla seleccionada.
					movimientoPosible = true;
				else if(piezaEnCasillaSeleccionada.getColor() == getColor())
				// La pieza en la casilla seleccionada es una pieza propia.
					retorno = EnumError.PIEZA_PROPIA;
				else {
				// La pieza en la casilla seleccionada es una pieza contraria.
					movimientoPosible = true;
					hayCaptura = true;
				}
			}
		}
		
		if(movimientoPosible) {
			quedariaEnJaque = false; // Esta instrucción es para evitar error de sintaxis.
			if(calcularJaque)
			// Se indicó por parámetro que se tiene que calcular el jaque.
				quedariaEnJaque = tablero.quedariaEnJaque(this, letraFinal, numeroFinal);
			if(calcularJaque && quedariaEnJaque)
			// El rey propio quedaría en jaque luego del movimiento,
			// o seguiría estándolo si ya lo estaba.
				if(estadoDelJuego == EnumEstadoDeJuego.PARTIDA_ACTIVA_EN_JAQUE)
					retorno = EnumError.MOVIMIENTO_INVALIDO_POR_JAQUE;
				else retorno = EnumError.RIESGO_DE_JAQUE;
			else {
			// Se indicó por parámetro que no se tiene que calcular el jaque,				
			// el rey propio no quedaría en jaque luego del movimiento,
			// o el rey propio saldría del jaque, si ya lo estaba.	
				if(realizarMovimiento) {
					if(hayCaptura)
					// Se realiza la captura de la pieza en la casilla seleccionada.
						tablero.quitarPiezaPorPosicion(letraFinal, numeroFinal);
	
					// Se realiza el movimiento.
					tablero.colocarPieza(this, letraFinal, numeroFinal);
					tablero.quitarPiezaPorPosicion(letraInicial, numeroInicial);
				}
				
				if(hayCaptura) retorno = EnumError.OK_MOVIMIENTO_Y_CAPTURA;
				else retorno = EnumError.SIN_ERROR;
			}
		}
		
		return retorno;
	}
	
	@Override
	public EnumTipoPieza consultarTipoDePieza() {
		return EnumTipoPieza.ALFIL;
	}

	@Override
	public Pieza devolerCopia() {
		return new Alfil(this.color);
	}

}