package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumTipoPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

class Peon extends Pieza {
	private boolean yaSeMovio, primerTurnoLuegoDeSalidaDoble;
	
	
	/* CONSTRUCTORES */
	
	Peon(EnumColorPieza color) {
		super();
		this.color = color;
		yaSeMovio = false;
		primerTurnoLuegoDeSalidaDoble = false;
	}

	private Peon(EnumColorPieza color, boolean yaSeMovio, boolean primerTurnoLuegoDeSalidaDoble) {
		super();
		this.color = color;
		this.yaSeMovio = yaSeMovio;
		this.primerTurnoLuegoDeSalidaDoble = primerTurnoLuegoDeSalidaDoble;
	}
	
	
	/* Métodos abstractos heredados */
	
	// Funcionamiento: ver método en superclase.
	@Override
	EnumError moverA(char letraFinal, int numeroFinal, EnumEstadoDeJuego estadoDelJuego, boolean realizarMovimiento, boolean calcularJaque) {
		EnumError retorno;
		boolean movimientoPosible, hayCaptura, hayCapturaAlPaso, quedariaEnJaque;
		char letraInicial;
		int numeroInicial;
		
		retorno = EnumError.SIN_ERROR; // Para evitar error de sintaxis.
		hayCaptura = false;
		hayCapturaAlPaso = false;
		letraInicial = tablero.consultarPosicionLetra(this);
		numeroInicial = tablero.consultarPosicionNumero(this);
		movimientoPosible = false;
		if(
			(
				this.getColor() == EnumColorPieza.BLANCA
				&&
				(
					(numeroFinal - numeroInicial) == 1
					&&
					letraFinal == letraInicial
				)
			)
			||
			(
				this.getColor() == EnumColorPieza.NEGRA
				&&
				(
					(numeroFinal - numeroInicial) == -1
					&&
					letraFinal == letraInicial
				)				
			)	
		)
		// La casilla seleccionada está una celda delante de la pieza.
			if(tablero.consultarPieza(letraFinal, numeroFinal) == null)
			// No hay pieza en la celda seleccionada.
				movimientoPosible = true;
			else retorno = EnumError.CASILLA_OCUPADA;
		else if(
			(
				(
					this.getColor() == EnumColorPieza.BLANCA
					&&
					(
						(numeroFinal - numeroInicial) == 2
						&&
						letraFinal == letraInicial
					)
				)
				||
				(
					this.getColor() == EnumColorPieza.NEGRA
					&&
					(
						(numeroFinal - numeroInicial) == -2
						&&
						letraFinal == letraInicial
					)
				)
			) && !yaSeMovio
		)
		// La casilla seleccionada está dos celdas delante de la pieza,
		// y la pieza todavía no se había movido.
			if(
				(
					this.getColor() == EnumColorPieza.BLANCA
					&&
					tablero.consultarPieza(letraInicial, numeroInicial + 1) != null
				)
				||
				(
					this.getColor() == EnumColorPieza.NEGRA
					&&
					tablero.consultarPieza(letraInicial, numeroInicial - 1) != null
				)
			)
			// La casilla justo delante de la pieza está ocupada.
				retorno = EnumError.SALTO_NO_PERMITIDO;
			else if(tablero.consultarPieza(letraFinal, numeroFinal) != null)
			// La casilla seleccionada no está libre.
				retorno = EnumError.CASILLA_OCUPADA;
			else
			// Se puede realizar el movimiento.			
				movimientoPosible = true;
		else if(
			(
				this.getColor() == EnumColorPieza.BLANCA
				&&
				(
					(numeroFinal - numeroInicial) == 1
					&&
					(
						(letraFinal - letraInicial) == 1
						||
						(letraFinal - letraInicial) == -1
					)
				)
			)
			||
			(
				this.getColor() == EnumColorPieza.NEGRA
				&&
				(
					(numeroFinal - numeroInicial) == -1
					&&
					(
						(letraFinal - letraInicial) == 1
						||
						(letraFinal - letraInicial) == -1
					)
				)				
			)	
		)
		// La casilla seleccionada se encuentra una casilla en diagonal
		// frente a la pieza.
			if(tablero.consultarPieza(letraFinal, numeroFinal) == null) {
			// En la casilla seleccionada no hay pieza.
				// Ver si se puede realizar el peón al paso.
				if(
					(
						this.getColor() == EnumColorPieza.BLANCA
						&&
						numeroInicial != 5
					)
					||
					(
						this.getColor() == EnumColorPieza.NEGRA
						&&
						numeroInicial != 4
					)
				)
				// El peón no está ubicado en la posición correcta para
				// realizar la captura de peón al paso.
					retorno = EnumError.MOVIMIENTO_INVALIDO;
				else if(
					(
						this.getColor() == EnumColorPieza.BLANCA
						&&
						tablero.consultarPieza(letraFinal, numeroFinal - 1).consultarTipoDePieza() != EnumTipoPieza.PEON
					)
					||
					(
						this.getColor() == EnumColorPieza.NEGRA
						&&
						tablero.consultarPieza(letraFinal, numeroFinal + 1).consultarTipoDePieza() != EnumTipoPieza.PEON
					)
				)
				// La pieza contra la que se quiere realizar el peón al paso no es un
				// peón.
					retorno = EnumError.MOVIMIENTO_INVALIDO;
				else if(
					(
						this.getColor() == EnumColorPieza.BLANCA
						&&
						((Peon) tablero.consultarPieza(letraFinal, numeroFinal - 1)).esPrimerTurnoLuegoDeSalidaDoble()
					)
					||
					(
						this.getColor() == EnumColorPieza.NEGRA
						&&
						((Peon) tablero.consultarPieza(letraFinal, numeroFinal + 1)).esPrimerTurnoLuegoDeSalidaDoble()
					)
				) {
				// El peón contra el que se quiere realizar el peón al paso se encuentra
				// en su primer turno luego de haber realizado un movimiento doble; lo que
				// significa que la captura de peón al paso se puede realizar.
					movimientoPosible = true;
					hayCapturaAlPaso = true;
				}
				else retorno = EnumError.MOVIMIENTO_INVALIDO;
			} else if(tablero.consultarPieza(letraFinal, numeroFinal).getColor() != this.getColor()) {
			// La pieza en la casilla seleccionada es del color contrario.
				movimientoPosible = true;
				hayCaptura = true;
			} else
			// La pieza en la casilla seleccionada es una pieza propia.
				retorno = EnumError.PIEZA_PROPIA;
		else
		// El movimiento que se quiere realizar es inválido.
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
					// Se realiza la captura normal.
						tablero.quitarPiezaPorPosicion(letraFinal, numeroFinal);
					else if(hayCapturaAlPaso && this.getColor() == EnumColorPieza.BLANCA)
					// Se realiza una captura al paso.
						tablero.quitarPiezaPorPosicion(letraFinal, numeroFinal - 1);
					else if(hayCapturaAlPaso && this.getColor() == EnumColorPieza.NEGRA)
					// Se realiza una captura al paso.
						tablero.quitarPiezaPorPosicion(letraFinal, numeroFinal + 1);
	
					// Se realiza el movimiento.
					tablero.colocarPieza(this, letraFinal, numeroFinal);
					tablero.quitarPiezaPorPosicion(letraInicial, numeroInicial);
					if(!yaSeMovio) yaSeMovio = true;
					if(letraFinal == letraInicial && Math.abs(numeroFinal - numeroInicial) == 2)
					// Si se realizó un movimiento inicial
						primerTurnoLuegoDeSalidaDoble = true;
				}
				
				if(hayCaptura || hayCapturaAlPaso) retorno = EnumError.OK_MOVIMIENTO_Y_CAPTURA;
				else retorno = EnumError.SIN_ERROR;
			}
		}
		
		return retorno;
	}

	// Funcionamiento: ver método en superclase.
	@Override
	EnumTipoPieza consultarTipoDePieza() {
		return EnumTipoPieza.PEON;
	}
	
	// Funcionamiento: ver método en superclase.
	@Override
	Pieza devolerCopia() {
		return new Peon(this.color, this.yaSeMovio, this.primerTurnoLuegoDeSalidaDoble);
	}
	
	
	/* Métodos propios */
	
	// Funcionamiento: devuelve true si es el primer turno del peón,
	// luego de haber hecho una salida doble.
	boolean esPrimerTurnoLuegoDeSalidaDoble() {
		return primerTurnoLuegoDeSalidaDoble;
	}
	
	// Funcionamiento: deja constancia de que ya pasó el primer
	// turno luego de haber hecho una salida doble.
	void unsetEsPrimerTurnoLuegoDeSalidaDoble() {
		primerTurnoLuegoDeSalidaDoble = false;
	}
}
