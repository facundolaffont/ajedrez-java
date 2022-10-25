package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumTipoPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

class Caballo extends Pieza {
	
	
	/* CONSTRUCTOR */
	
	Caballo(EnumColorPieza color) {
		super();
		this.color = color;
	}

	
	/* Métodos abstractos heredados */
	
	// Funcionamiento: ver método en superclase.
	@Override
	EnumError moverA(char letraFinal, int numeroFinal, EnumEstadoDeJuego estadoDelJuego, boolean realizarMovimiento, boolean calcularJaque) {
		EnumError retorno;
		boolean movimientoPosible, hayCaptura, quedariaEnJaque;
		char letraInicial;
		int numeroInicial;
		Pieza piezaEnCasillaSeleccionada;
		
		retorno = EnumError.SIN_ERROR; // Para evitar error de sintaxis.
		piezaEnCasillaSeleccionada = null; // Para evitar error de sintaxis.
		hayCaptura = false;
		letraInicial = tablero.consultarPosicionLetra(this);
		numeroInicial = tablero.consultarPosicionNumero(this);
		movimientoPosible = false;
		if(
			(
				Math.abs(letraFinal - letraInicial) == 2
				&&
				Math.abs(numeroFinal - numeroInicial) == 1
			)
			||
			(
				Math.abs(letraFinal - letraInicial) == 1
				&&
				Math.abs(numeroFinal - numeroInicial) == 2
			)			
		) {
		// El movimiento que se intenta realizar es válido.
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
		} else
		// El movimiento que se intenta realizar es inválido.
			retorno = EnumError.CASILLA_INVALIDA;
		
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
	
	// Funcionamiento: ver método en superclase.
	@Override
	EnumTipoPieza consultarTipoDePieza() {
		return EnumTipoPieza.CABALLO;
	}

	// Funcionamiento: ver método en superclase.
	@Override
	Pieza devolerCopia() {
		return new Caballo(this.color);
	}
}
