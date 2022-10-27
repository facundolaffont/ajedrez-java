package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumTipoPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

class Rey extends Pieza {
	
	/* Miembros públicos. */
	
	public Rey(EnumColorPieza color) {
		super();
		this.color = color;	
		yaSeMovio = false;
	}
	
	@Override
	public EnumError moverA(char letraFinal, int numeroFinal, EnumEstadoDeJuego estadoDelJuego, boolean realizarMovimiento, boolean calcularJaque) {
		EnumError retorno;
		boolean movimientoPosible, enroquePosible, hayCaptura, hayPieza,		
		quedariaEnJaque, estariaEnJaque;
		char letraInicial;
		int numeroInicial, offsetDeLetra, indiceDeCasilla, cantidadCasillas;
		Pieza torreDeEnroque;
		
		retorno = EnumError.SIN_ERROR; // Instrucción para evitar error de sintaxis.
		hayCaptura = false;
		letraInicial = tablero.consultarPosicionLetra(this);
		numeroInicial = tablero.consultarPosicionNumero(this);
		movimientoPosible = false;
		enroquePosible = false;
		if(
			(
				letraFinal == letraInicial
				&&
				Math.abs(numeroFinal - numeroInicial) == 1
			)
			||
			(
				numeroFinal == numeroInicial
				&&
				Math.abs(letraFinal - letraInicial) == 1
			)
			||
			(
				Math.abs(letraFinal - letraInicial) == 1
				&&
				Math.abs(numeroFinal - numeroInicial) == 1
			)
		)
		// El movimiento que se quiere realizar es legal para la pieza,
		// y no es un roque.
			if(tablero.consultarPieza(letraFinal, numeroFinal) == null)
			// No hay pieza en la casilla seleccionada.
				movimientoPosible = true;
			else if(tablero.consultarPieza(letraFinal, numeroFinal).getColor() == this.getColor())
			// La pieza que se encuentra en la casilla seleccionada es una
			// pieza propia.
				retorno = EnumError.PIEZA_PROPIA;
			else {
			// La pieza que se encuentra en la casilla seleccionada es una
			// pieza contraria.
				movimientoPosible = true;
				hayCaptura = true;
			}
		else if(!yaSeMovio && numeroFinal == numeroInicial && Math.abs(letraFinal - letraInicial) == 2)
		// Se intenta hacer un enroque.
			if(estadoDelJuego == EnumEstadoDeJuego.PARTIDA_ACTIVA_EN_JAQUE)
			// La pieza está en jaque.
				retorno = EnumError.MOVIMIENTO_INVALIDO;
			else {
			// La pieza no está en jaque.
				// Guarda en una variable la cantidad de casillas
				// a recorrer.
				cantidadCasillas = Math.abs(letraFinal - letraInicial);
				
				// Chequeamos si hay alguna pieza entre la casilla
				// seleccionada y la casilla en la que se encuentra el alfil.
				if(letraFinal > letraInicial) offsetDeLetra = +1;
				else offsetDeLetra = -1;			
				hayPieza = false;
				indiceDeCasilla = 1;
				if(
					letraFinal < letraInicial
					&&
					tablero.consultarPieza((char) (letraFinal - 1), numeroFinal) != null
				) hayPieza = true;
				while(!hayPieza && indiceDeCasilla <= cantidadCasillas) {
					if(tablero.consultarPieza(
						(char) (letraInicial + (offsetDeLetra * indiceDeCasilla)),
						numeroInicial
					) != null)
						hayPieza = true;
					indiceDeCasilla++;
				}
				
				if(hayPieza)
				// Hay una pieza entre la torre y el rey.
					retorno = EnumError.MOVIMIENTO_INVALIDO;
				else
				// No hay pieza entre el rey y la torre.				
					if(this.yaSeMovio)
					// El rey ya se había movido previamente.
						retorno = EnumError.MOVIMIENTO_INVALIDO;
					else if(calcularJaque) {
					// Se indicó por parámetro que se calcule el jaque.
						indiceDeCasilla = 1;
						estariaEnJaque = false;
						while(!estariaEnJaque && indiceDeCasilla <= cantidadCasillas) {
							if(tablero.quedariaEnJaque(
								this,
								(char) (letraInicial + (offsetDeLetra * indiceDeCasilla)),
								numeroInicial
							))
								estariaEnJaque = true;
							indiceDeCasilla++;
						}
						
						if(estariaEnJaque)
							retorno = EnumError.MOVIMIENTO_INVALIDO;
						else
							enroquePosible = true;
					} else
					// No se indicó por parámetro que se calcule el jaque.
						enroquePosible = true;
			}
		else
		// El movimiento que se quiere realizar es inválido.
			retorno = EnumError.CASILLA_INVALIDA;
		
		if(movimientoPosible) {
		// El movimiento que se quiere realizar no es un enroque.
			quedariaEnJaque = false; // Esta instrucción es para evitar error de sintaxis.
			if(calcularJaque)
			// Se indicó por parámetro que se tiene que calcular el jaque.
				quedariaEnJaque = tablero.quedariaEnJaque(this, letraFinal, numeroFinal);
			if(calcularJaque && quedariaEnJaque)
			// La pieza quedaría en jaque luego del movimiento,
			// o seguiría estándolo si ya lo estaba.
				if(estadoDelJuego == EnumEstadoDeJuego.PARTIDA_ACTIVA_EN_JAQUE)
					retorno = EnumError.MOVIMIENTO_INVALIDO_POR_JAQUE;
				else retorno = EnumError.RIESGO_DE_JAQUE;
			else {
			// Se indicó por parámetro que no se tiene que calcular el jaque,				
			// la pieza no quedaría en jaque luego del movimiento, saldría del
			// jaque, si ya lo estaba.	
				if(realizarMovimiento) {
					if(hayCaptura)
					// Se realiza la captura de la pieza en la casilla seleccionada.
						tablero.quitarPiezaPorPosicion(letraFinal, numeroFinal);
	
					// Se realiza el movimiento.
					tablero.colocarPieza(this, letraFinal, numeroFinal);
					tablero.quitarPiezaPorPosicion(letraInicial, numeroInicial);
					if(!yaSeMovio) yaSeMovio = true;
				}
				
				if(hayCaptura) retorno = EnumError.OK_MOVIMIENTO_Y_CAPTURA;
				else retorno = EnumError.SIN_ERROR;
			}
		} else if(enroquePosible) {
		// El movimiento que se quiere realizar es un enroque.
			if(realizarMovimiento) {
			// Se indicó por parámetro que se quiere realizar efectivamente
			// el movimiento en el tablero.
				// Mueve el rey.
				tablero.colocarPieza(this, letraFinal, numeroFinal);
				tablero.quitarPiezaPorPosicion(letraInicial, numeroInicial);
				
				// Mueve la torre.
				if(letraFinal > letraInicial) offsetDeLetra = +1;
				else offsetDeLetra = -2;			
				torreDeEnroque = tablero.consultarPieza((char) (letraFinal + offsetDeLetra), numeroFinal);
				((Torre) torreDeEnroque).enrocar();
				tablero.quitarPiezaPorPosicion((char) (letraFinal + offsetDeLetra), numeroFinal);
				
				// Modifica el estado del rey para determinar que ya se realizó
				// el primer movimiento.
				yaSeMovio = true;
			}
			
			retorno = EnumError.SIN_ERROR;
		}
		
		return retorno;
	}
	
	@Override
	public EnumTipoPieza consultarTipoDePieza() {
		return EnumTipoPieza.REY;
	}

	@Override
	public Pieza devolerCopia() {
		return new Rey(this.color, this.yaSeMovio);
	}


	/* Miembros privados. */

	private boolean yaSeMovio;

	private Rey(EnumColorPieza color, boolean yaSeMovio) {
		super();
		this.color = color;
		this.yaSeMovio = yaSeMovio;
	}
	
}
