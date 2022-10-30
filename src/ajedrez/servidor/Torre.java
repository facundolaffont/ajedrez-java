package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumTipoPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

class Torre extends Pieza {
	
	/* Miembros públicos. */
	
	public Torre(EnumColorPieza color) {
		super();
		this.color = color;
		yaSeMovio = false;
	}
	
	/* Métodos abstractos heredados */
	
	// Funcionamiento: ver método en superclase.
	@Override
	public int moverA(char letraFinal, int numeroFinal, EnumEstadoDeJuego estadoDelJuego, boolean realizarMovimiento, boolean calcularJaque) {
		/*
		EnumError retorno;
		boolean movimientoPosible, hayPieza, hayCaptura, quedariaEnJaque;
		char letraInicial;
		int numeroInicial, cantidadCasillas, offsetDeLetra, offsetDeNumero,
		indiceDeCasilla;
		Pieza piezaEnCasillaSeleccionada;
		
		retorno = EnumError.SIN_ERROR; // Instrucción para evitar error de sintaxis.
		piezaEnCasillaSeleccionada = null; // Instrucción para evitar error de sintaxis.
		hayPieza = false;
		hayCaptura = false;
		letraInicial = tablero.consultarPosicionLetra(this);
		numeroInicial = tablero.consultarPosicionNumero(this);
		movimientoPosible = false;
		if(letraFinal == letraInicial) {
		// El movimiento que se quiere realizar es vertical.
			// Guarda en una variable la cantidad de casillas
			// a recorrer.
			cantidadCasillas = Math.abs(numeroFinal - numeroInicial);
			
			// Chequeamos si hay alguna pieza entre la casilla
			// seleccionada y la casilla en la que se encuentra la dama.
			if(numeroFinal > numeroInicial) offsetDeNumero = +1;
			else offsetDeNumero = -1;
			indiceDeCasilla = 1;
			while(!hayPieza && indiceDeCasilla < cantidadCasillas) {
				if(tablero.consultarPieza(
					letraInicial,
					numeroInicial + (offsetDeNumero * indiceDeCasilla)
				) != null)
					hayPieza = true;
				indiceDeCasilla++;
			}
		} else if(numeroFinal == numeroInicial) {
		// El movimiento que se quiere realizar es horizontal.
			// Guarda en una variable la cantidad de casillas
			// a recorrer.
			cantidadCasillas = Math.abs(letraFinal - letraInicial);
			
			// Chequeamos si hay alguna pieza entre la casilla
			// seleccionada y la casilla en la que se encuentra la dama.
			if(letraFinal > letraInicial) offsetDeLetra = +1;
			else offsetDeLetra = -1;						
			indiceDeCasilla = 1;
			while(!hayPieza && indiceDeCasilla < cantidadCasillas) {
				if(tablero.consultarPieza(
					(char) (letraInicial + (offsetDeLetra * indiceDeCasilla)),
					numeroInicial
				) != null)
					hayPieza = true;
				indiceDeCasilla++;
			}
		} else
		// El movimiento que se quiere realizar es inválido.
			retorno = EnumError.CASILLA_INVALIDA;		
		
		if(retorno == EnumError.SIN_ERROR)
			if(hayPieza)
			// Hay una pieza entre la casilla seleccionada y la dama.
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
					if(!yaSeMovio) yaSeMovio = true;
				}
				
				if(hayCaptura) retorno = EnumError.OK_MOVIMIENTO_Y_CAPTURA;
				else retorno = EnumError.SIN_ERROR;
			}
		}
		*/

		return 0;
	}
	
	// Funcionamiento: ver método en superclase.
	@Override
	public EnumTipoPieza consultarTipoDePieza() {
		return EnumTipoPieza.TORRE;
	}

	// Funcionamiento: ver método en superclase.
	@Override
	public Pieza devolerCopia() {
		return new Torre(this.color, this.yaSeMovio);
	}
	
	// Condiciones previas: sólo lo puede llamar el rey cuando realiza un enroque.
	//
	// Funcionamiento: realiza la parte final del movimiento del enroque,
	// una vez que el rey haya realizado la primera parte del enroque.
	public void enrocar() {
		/*
		char letraActual;
		int numeroActual;
		
		letraActual = tablero.consultarPosicionLetra(this);
		numeroActual = tablero.consultarPosicionNumero(this);
		if(letraActual == 'A')
		// La torre está en el casillero con letra 'A'.
			tablero.colocarPieza(this, (char) (letraActual + 3), numeroActual);			
		else
		// La torre está en el casillero con letra 'H'.
			tablero.colocarPieza(this, (char) (letraActual - 2), numeroActual);
		
		// Modifica el estado de la torre para indicar que ya se realizó el
		// primer movimiento.
		yaSeMovio = true;
		*/
	}

	
	/* Miembros privados. */

	private boolean yaSeMovio;

	private Torre(EnumColorPieza color, boolean yaSeMovio) {
		super();
		this.color = color;
		this.yaSeMovio = yaSeMovio;
	}
}
