package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumTipoPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

abstract class Pieza {
	protected EnumColorPieza color;
	protected Tablero tablero;
	
	
	/* CONSTRUCTOR */
	
	Pieza() {
		tablero = null;
	}
	
	
	/* Métodos abstractos */
	
	// Condiciones previas: la letra y número pasados por parámetro deben ser valores
	// válidos para el tablero de ajedrez (i.e 'A'-'H' y 1-8).
	//
	// Funcionamiento:
	abstract EnumError moverA(char letraFinal, int numeroFinal, EnumEstadoDeJuego estadoDelJuego, boolean realizarMovimiento, boolean calcularJaque);
	
	// Funcionamiento: devuelve el tipo de la pieza.
	abstract EnumTipoPieza consultarTipoDePieza();
	
	// Funcionamiento: devuelve un puntero a una copia nueva, exacta de esta pieza.
	abstract Pieza devolerCopia();
	
	
	/* Métodos no abstractos */
	
	// Funcionamiento: devuelve el color de la pieza.
	EnumColorPieza getColor() {
		return color;
	}
	
	// Funcionamiento: devuelve la letra de la posición en el
	// tablero en el que se encuentra, si se encuentra en uno;
	// o 'X' si no se encuentra en un tablero.
	char consultarPosicionLetra() {
		return tablero.consultarPosicionLetra(this);
	}
	
	// Funcionamiento: devuelve el número de la posición en el
	// tablero en el que se encuentra, si se encuentra en uno;
	// o 0 si no se encuentra en un tablero.
	int consultarPosicionNumero() {
		return tablero.consultarPosicionNumero(this);
	}

	// Funcionamiento: establece el puntero al tablero al que
	// pertenece la pieza.
	void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}

	// Funcionamiento: devuelve el puntero al tablero en el que
	// se encuentra la pieza, si se encuentra en un tablero; o
	// devuelve null si no se encuentra en ningún tablero.
	Tablero getTablero() {
		return tablero;
	}
	
	// Condiciones previas: la pieza debe estar en el tablero.
	//
	// Funcionamiento: quita a la pieza de su tablero, y deja
	// el puntero "tablero" que se encuentra dentro de la pieza
	// en null.
	void quitarDelTablero() {
		tablero.quitarPiezaPorInstancia(this);
		tablero = null;
	}
	
	// Funcionamiento: calcula en cada casilla del tablero, a excepción de la
	// casilla en la que se encuentra la pieza que realiza el cálculo, si se puede
	// realizar un movimiento. Si hay, al menos uno que se pueda realizar, esta
	// función devuelve true; si no, devuelve false.
	boolean hayAlgunMovimientoValido() {
		EnumError resultado;
		boolean retorno;
		
		retorno = false;
		for(char letraFinal = 'A'; letraFinal <= 'H'; letraFinal++)
			for(int numeroFinal = 1; numeroFinal <= 8; numeroFinal++)
				if(
					!(
						this.consultarPosicionLetra() == letraFinal
						&&
						this.consultarPosicionNumero() == numeroFinal						
					)
				) {
				// La casilla a la que se quiere mover es distinta a la casilla en
				// la que se encuentra la pieza.
					resultado = this.moverA(letraFinal, numeroFinal, this.tablero.consultarEstadoDelJuego(), false, true);
					if(resultado == EnumError.SIN_ERROR || resultado == EnumError.OK_MOVIMIENTO_Y_CAPTURA)
					// Se puede realizar el movimiento.
						retorno = true;
				}
				
		return retorno;
	}
}
