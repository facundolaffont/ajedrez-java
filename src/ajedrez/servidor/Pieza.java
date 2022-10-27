package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumTipoPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

abstract class Pieza {
	
	/* Miembros públicos. */
	
	public Pieza() { tablero = null; }
	
	public EnumColorPieza getColor() { return color; }
	
	// Devuelve la letra de la posición en el tablero en
	// el que se encuentra, si se encuentra en uno; o 'X'
	// si no se encuentra en un tablero.
	public char consultarPosicionLetra() { return tablero.consultarPosicionLetra(this); }
	
	// Devuelve el número de la posición en el tablero en
	// el que se encuentra, si se encuentra en uno; o 0 si
	// no se encuentra en un tablero.
	public int consultarPosicionNumero() { return tablero.consultarPosicionNumero(this); }

	public void setTablero(Tablero tablero) { this.tablero = tablero; }

	// Devuelve el puntero al tablero en el que se encuentra
	// la pieza, si se encuentra en un tablero; o devuelve null
	// si no se encuentra en ningún tablero.
	public Tablero getTablero() { return tablero; }
	
	// Condiciones previas: la pieza debe estar en el tablero.
	//
	// Quita a la pieza de su tablero, y deja el puntero "tablero"
	// que se encuentra dentro de la pieza en null.
	public void quitarDelTablero() {

		tablero.quitarPiezaPorInstancia(this);
		tablero = null;

	}
	
	// Calcula en cada casilla del tablero, a excepción de la casilla
	// en la que se encuentra la pieza que realiza el cálculo, si se puede
	// realizar un movimiento. Si hay, al menos uno que se pueda
	// realizar, esta función devuelve true; si no, devuelve false.
	public boolean hayAlgunMovimientoValido() {
		
		boolean retorno = false;
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
					EnumError resultado = this.moverA(letraFinal, numeroFinal, this.tablero.consultarEstadoDelJuego(), false, true);
					if(resultado == EnumError.SIN_ERROR || resultado == EnumError.OK_MOVIMIENTO_Y_CAPTURA)
					// Se puede realizar el movimiento.
						retorno = true;
				}
				
		return retorno;

	}


	/* Miembros protegidos. */

	protected EnumColorPieza color;
	protected Tablero tablero;

	
	/* Miembros abstractos. */
	
	// Condiciones previas: la letra y número pasados por parámetro deben ser valores
	// válidos para el tablero de ajedrez (i.e 'A'-'H' y 1-8).
	//
	// Funcionamiento: realiza un movimiento de la pieza y devuelve el estado.
	abstract EnumError moverA(char letraFinal, int numeroFinal, EnumEstadoDeJuego estadoDelJuego, boolean realizarMovimiento, boolean calcularJaque);
	
	// Devuelve el tipo de la pieza.
	abstract EnumTipoPieza consultarTipoDePieza();
	
	// Devuelve un puntero a una copia nueva de esta pieza.
	abstract Pieza devolerCopia();

}