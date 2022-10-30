package ajedrez.servidor;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumTipoPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

class Tablero {
	
	/* Miembros públicos. */
	
	public Tablero(Juego juego) {
		_casillas = new Pieza[64];
		_juego = juego;
	}
	
	public Tablero(Juego juego, Pieza[] casillas) {
		casillas = new Pieza[64];
		_juego = juego;
		
		// Realiza una copia exacta del parámetro "casillas", generando
		// piezas totalmente independientes, con los mismos parámetros.
		for(int i = 0; i < 64; i++)
			if(casillas[i] != null)
				casillas[i] = casillas[i].devolerCopia();
		
		// Vincula a todas las piezas del tablero con el tablero actual.
		for(Pieza p: casillas)
			if(p != null)
				p.setTablero(this);
	}
	
	
	/* Miembros privados. */

	private Pieza[] _casillas;
	private Juego _juego;
	
	// Condiciones previas: las letras y números deben ser valores
	// válidos para un tablero de ajedrez.
	//
	// Funcionamiento: devuelve el índice correspondiente para el
	// arreglo de casillas en la que se encuentra la coordenada
	// equivalente. El orden del arreglo "casillas" será el
	// siguiente (mostraremos un número, que corresponderá al índice
	// del arreglo, seguido de una letra y un número entre paréntesis
	// que corresponderá a la posición de tablero equivalente):
	//
	//  0(A1)	1(A2)	2(A3)	3(A4)	4(A5)	5(A6)	6(A7)	7(A8)
	//
	//  8(B1)	9(B2)	10(B3)	11(B4)	12(B5)	13(B6)	14(B7)	15(B8)
	//
	//	16(C1)	17(C2)	18(C3)	19(C4)	20(C5)	21(C6)	22(C7)	23(C8)
	//
	//	24(D1)	25(D2)	26(D3)	27(D4)	28(D5)	29(D6)	30(D7)	31(D8)
	//
	//	32(E1)	33(E2)	34(E3)	35(E4)	36(E5)	37(E6)	38(E7)	39(E8)
	//
	//	40(F1)	41(F2)	42(F3)	43(F4)	44(F5)	45(F6)	46(F7)	47(F8)
	//
	//	48(G1)	49(G2)	50(G3)	51(G4)	52(G5)	53(G6)	54(G7)	55(G8)
	//
	//	56(H1)	57(H2)	58(H3)	59(H4)	60(H5)	61(H6)	62(H7)	63(H8)
	//   
	private int transformarCoordenadas(char letra, int numero) {
		int valorLetra;
		
		valorLetra = 0; // Inicializo para evitar error de sintaxis.
		switch(letra) {
			case 'A': valorLetra = 0; break;	
			case 'B': valorLetra = 1; break;
			case 'C': valorLetra = 2; break;
			case 'D': valorLetra = 3; break;
			case 'E': valorLetra = 4; break;
			case 'F': valorLetra = 5; break;
			case 'G': valorLetra = 6; break;
			case 'H': valorLetra = 7;
		}
		
		return (valorLetra * 8) + (numero - 1);		
	}
	
	// Condiciones previas: el índice pasado por parámetro debe ser
	// un índice válido para el arreglo interno de casillas.
	//
	// Funcionamiento: devuelve la letra de la posición equivalente
	// del arreglo interno de casillas.
	private char transformarIndiceALetra(int indice) {
		
		char retorno;
		if(indice < 8) retorno = 'A';
		else if(indice < 16) retorno = 'B';
		else if(indice < 24) retorno = 'C';
		else if(indice < 32) retorno = 'D';
		else if(indice < 40) retorno = 'E';
		else if(indice < 48) retorno = 'F';
		else if(indice < 56) retorno = 'G';
		else retorno = 'H';
		
		return retorno;

	}
	
	// Condiciones previas: el índice pasado por parámetro debe ser
	// un índice válido para el arreglo interno de casillas.
	//
	// Funcionamiento: devuelve el número de la posición equivalente
	// del arreglo interno de casillas.
	private int transformarIndiceANumero(int indice) { return (indice % 8) + 1; }
	
	// Condiciones previas: la letra y número deben ser valores válidos
	// para el tablero de ajedrez (i.e. A-H y 1-8).
	//
	// Funcionamiento: el apuntador de la celda correspondiente del
	// arreglo interno apuntará a la pieza pasada como parámetro, si
	// la celda no apuntaba previamente a ninguna pieza, y el método
	// devuelve true; o devolverá false si la celda apuntaba a una pieza,
	// y no se realizan modificaciones en el arreglo interno. La colocación
	// de una pieza no implica necesariamente que se trata de un movimiento
	// (esto está pensado así para un eventual uso de colocar piezas por
	// primera vez en el tablero).
	private boolean colocarPieza(Pieza pieza, char letra, int numero) {
		
		boolean retorno;
		if(consultarPieza(letra, numero) == null) {
			_casillas[transformarCoordenadas(letra, numero)] = pieza;
			retorno = true;
		} else retorno = false;
		
		return retorno;

	}
	
	// Condiciones previas: la letra y número deben ser valores válidos
	// para el tablero de ajedrez (i.e. A-H y 1-8).
	//
	// Funcionamiento: devuelve null si no hay pieza en la posición
	// especificada, o devuelve la Pieza, de lo contrario.
	private Pieza consultarPieza(char letra, int numero) { return _casillas[transformarCoordenadas(letra, numero)]; }
	
	// Condiciones previas: la pieza pasada debe pertenecer al tablero.
	//
	// Funcionamiento: si la pieza pasada por parámetro se encuentra
	// dentro del tablero, devuelve la letra de su posición; si no,
	// devuelve 'X'.
	private char consultarPosicionLetra(Pieza pieza) {

		char retorno = 'x';
		int indice = 0;
		while(retorno == 'x' && indice < 64) {
			if(_casillas[indice] == pieza)
				retorno = transformarIndiceALetra(indice);
			indice++;
		}
				
		return retorno;

	}
	
	// Funcionamiento: si la pieza pasada por parámetro se encuentra
	// dentro del tablero, devuelve la letra de su posición; si no,
	// devuelve 0.
	private int consultarPosicionNumero(Pieza pieza) {
		
		int retorno = 0;
		int indice = 0;
		while(retorno == 0 && indice < 64) {
			if(_casillas[indice] == pieza)
				retorno = transformarIndiceANumero(indice);
			indice++;
		}
				
		return retorno;

	}
	
	// Condiciones previas: la pieza debe pertenecer al tablero.
	//
	// Funcionamiento: busca la posición del arreglo que apunta hacia
	// la pieza pasada como parámetro y establece el puntero en null.
	private void quitarPiezaPorInstancia(Pieza pieza) {
		
		boolean fin = false;
		int indice = 0;
		while(!fin)
			if(_casillas[indice] == pieza) {
				_casillas[indice] = null;
				fin = true;
			} else indice++;

	}
	
	// Condiciones previas: debe haber una pieza en la posición indicada.
	//
	// Funcionamiento: el objeto del arreglo en la posición indicada
	// apuntará a null.
	private void quitarPiezaPorPosicion(char letra, int numero) {
		_casillas[transformarCoordenadas(letra, numero)] = null;
	}
	
	// Funcionamiento: devuelve la información del tablero representada
	// en Strings, de tal forma que, las piezas serán representadas
	// de la siguiente manera:
	//
	// 		"p": peón negro.
	//		"P": peón blanco.
	//		"t": torre negra.
	//		"T": torre blanca.
	//		"c": caballo negro.
	//		"C": caballo blanco.
	//		"a": alfil negro.
	//		"A": alfil blanco.
	//		"d": dama negra.
	//		"D": dama blanca.
	//		"r": rey negro.
	//		"R": rey blanco.
	//
	// En las posiciones en las que no haya piezas, devuelve un String vacío.
	private String[] obtenerInfoDeTablero() {
		String tableroDeInfo[];
		
		tableroDeInfo = new String[64];
		for(int i = 0; i < 64; i++)
			if(_casillas[i] == null) tableroDeInfo[i] = "";
			else switch(_casillas[i].consultarTipoDePieza()) {
				case PEON:
					if(_casillas[i].getColor() == EnumColorPieza.BLANCA) tableroDeInfo[i] = "P";
					else tableroDeInfo[i] = "p"; break;
				case TORRE:
					if(_casillas[i].getColor() == EnumColorPieza.BLANCA) tableroDeInfo[i] = "T";
					else tableroDeInfo[i] = "t"; break;
				case CABALLO:
					if(_casillas[i].getColor() == EnumColorPieza.BLANCA) tableroDeInfo[i] = "C";
					else tableroDeInfo[i] = "c"; break;
				case ALFIL:
					if(_casillas[i].getColor() == EnumColorPieza.BLANCA) tableroDeInfo[i] = "A";
					else tableroDeInfo[i] = "a"; break;
				case DAMA:
					if(_casillas[i].getColor() == EnumColorPieza.BLANCA) tableroDeInfo[i] = "D";
					else tableroDeInfo[i] = "d"; break;
				case REY:
					if(_casillas[i].getColor() == EnumColorPieza.BLANCA) tableroDeInfo[i] = "R";
					else tableroDeInfo[i] = "r"; break;
				case SIN_TIPO: // Esta línea es para evitar error de sintaxis.
			}
		
		return tableroDeInfo;
	}

	// Condiciones previas: el movimiento debe ser, además de un movimiento legal,
	// un movimiento que termine sobre una casilla en la que se encuentra
	// una pieza de otro color, sin importar si luego de la captura el rey
	// propio quedaría en jaque, ya que esto último lo determina el mismo
	// método.
	//
	// Funcionamiento: la pieza pasada por parámetro realiza el movimiento
	// a la casilla pasada por parámetro. Si luego del movimiento el rey del
	// mismo color queda en jaque, este método devuelve true; si no, devuelve
	// false.
	private boolean quedariaEnJaque(Pieza pieza, char letraFinal, int numeroFinal) {
		Tablero tableroTemp;
		char letraInicial;
		int numeroInicial;
		Pieza piezaAMover;
		
		// Crea una copia exacta del tablero actual.
		tableroTemp = new Tablero(_juego, _casillas);
		
		// Obtiene la posición de la pieza del tablero original.
		letraInicial = pieza.getTablero().consultarPosicionLetra(pieza);
		numeroInicial = pieza.getTablero().consultarPosicionNumero(pieza);
		
		// Obtiene el puntero a la pieza que se encuentra en el mismo lugar
		// pero en el nuevo tablero.
		piezaAMover = tableroTemp.consultarPieza(letraInicial, numeroInicial);
		
		// Realiza el movimiento sobre el tablero temporal.		
		piezaAMover.moverA(letraFinal, numeroFinal, _juego.getEstadoDeJuego(), true, false);
		
		return tableroTemp.estaEnJaque(pieza.getColor(), true);
	}
	
	// Funcionamiento: devuelve true si el rey del color pasado por parámetro
	// está en jaque; si no, devuelve false. Para analizar esto, se selecciona
	// cada pieza del color contrario y se calcula si se puede mover a la
	// casilla en la que se encuentra el rey del color pasado por parámetro.
	//
	// Si "paraCalcularJaqueMate" es false, por cada pieza del color contrario
	// que se pueda mover a donde está el rey del color pasado por parámetro,
	// se calcula si ese movimiento produciría un jaque en el rey del color
	// contrario; en tal caso, el movimiento no cuenta como válido; entonces,
	// si existe al menos una pieza que cumpla con un movimiento totalmente
	// válido, el método devuelve true; si no, false.
	//
	// Si "paraCalcularJaqueMate" es true, dicho cálculo posterior que intenta
	// determinar si el rey del color contrario estaría en jaque, no se realiza.
	// Este cálculo es especialmente útil cuando a este método se lo llama desde
	// quedariaEnJaque de la clase Tablero.
	private boolean estaEnJaque(EnumColorPieza turno, boolean paraCalcularJaqueMate) {
		/*
		Pieza rey;
		int indice, numero;
		char letra;
		boolean estaEnJaque, calculaElJaqueDelPropioReyCuandoMueve;
		
		// Encuentra al rey del color pasado por parámetro.
		rey = null;
		indice = 0;
		while(rey == null && indice < 64)
			if(
				_casillas[indice] != null
				&&
				_casillas[indice].getColor() == turno
				&&
				_casillas[indice].consultarTipoDePieza() == EnumTipoPieza.REY
			)
				rey = _casillas[indice];
			else indice++;
		
		// Obtiene la posición de dicho rey.
		letra = rey.consultarPosicionLetra();
		numero = rey.consultarPosicionNumero();
		
		// Consulta a cada pieza contraria si podría capturar al rey en el próximo
		// movimiento.
		estaEnJaque = false;
		indice = 0;
		if(paraCalcularJaqueMate) calculaElJaqueDelPropioReyCuandoMueve = false;
		else calculaElJaqueDelPropioReyCuandoMueve = true;
		while(!estaEnJaque && indice < 64)
			if(
				_casillas[indice] != null
				&&
				_casillas[indice].getColor() != turno
				&&
				_casillas[indice].moverA(letra, numero, _juego.getEstadoDeJuego(), false, calculaElJaqueDelPropioReyCuandoMueve) == EnumError.SIN_ERROR
			)
				estaEnJaque = true;
			else indice++;
		
		return estaEnJaque;
	}
	
	// Funcionamiento: actualiza el estado de todos los peones del color
	// pasado por parámetro, de modo tal que se sepa cuáles dejarán de poder
	// ser capturados al paso.
	private void actualizarEstadoDePeones(EnumColorPieza turno) {

		for(Pieza p: _casillas)
			if(
				p != null
				&& p.getColor() == turno
				&& p.consultarTipoDePieza() == EnumTipoPieza.PEON
				&& ( (Peon) p ).esPrimerTurnoLuegoDeSalidaDoble()
			)
				( (Peon) p ).unsetEsPrimerTurnoLuegoDeSalidaDoble();

	}

	// Condiciones previas: el rey del color pasado por parámetro debe ya
	// encontrarse en jaque.
	//
	// Funcionamiento: se analiza si el rey del color pasado por parámetro
	// está en jaque mate.
	private boolean estaEnJaqueMate(EnumColorPieza turno) {
		Pieza rey;
		int indice, numero;
		char letra;
		boolean estariaEnJaque, retorno;
		
		// Encuentra al rey del color pasado por parámetro.
		rey = null;
		indice = 0;
		while(rey == null && indice < 64)
			if(
				_casillas[indice] != null
				&&
				_casillas[indice].getColor() == turno
				&&
				_casillas[indice].consultarTipoDePieza() == EnumTipoPieza.REY
			)
				rey = _casillas[indice];
			else indice++;
		
		// Obtiene la posición de dicho rey.
		letra = rey.consultarPosicionLetra();
		numero = rey.consultarPosicionNumero();
		
		// Consulta si hay al menos un movimiento posible que pueda realizar el
		// rey en el que no estaría en jaque.
		estariaEnJaque = true;
		if( (char) (letra + 1) <= 'H' ) {
			EnumError resultadoDelMovimiento = rey.moverA((char) (letra + 1), numero, _juego.getEstadoDeJuego(), false, true)
			if (
				resultadoDelMovimiento == EnumError.SIN_ERROR
				||
				resultadoDelMovimiento == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			) estariaEnJaque = false;
		} else if(
			(char) (letra + 1) <= 'H'
			&&
			numero + 1 <= 8
			&&
			(
				rey.moverA((char) (letra + 1), numero + 1, _juego.getEstadoDeJuego(), false, true) == EnumError.SIN_ERROR
				||
				rey.moverA((char) (letra + 1), numero + 1, _juego.getEstadoDeJuego(), false, true) == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			)
		)
			estariaEnJaque = false;
		else if(
			numero + 1 <= 8
			&&
			(
				rey.moverA(letra, numero + 1, _juego.getEstadoDeJuego(), false, true) == EnumError.SIN_ERROR
				||
				rey.moverA(letra, numero + 1, _juego.getEstadoDeJuego(), false, true) == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			)
		)
			estariaEnJaque = false;
		else if(
			(char) (letra - 1) >= 'A'
			&&
			numero + 1 <= 8
			&&
			(
				rey.moverA((char) (letra - 1), numero + 1, _juego.getEstadoDeJuego(), false, true) == EnumError.SIN_ERROR
				||
				rey.moverA((char) (letra - 1), numero + 1, _juego.getEstadoDeJuego(), false, true) == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			)
		)
			estariaEnJaque = false;
		else if(
			(char) (letra - 1) >= 'A'
			&&
			(
				rey.moverA((char) (letra - 1), numero, _juego.getEstadoDeJuego(), false, true) == EnumError.SIN_ERROR
				||
				rey.moverA((char) (letra - 1), numero, _juego.getEstadoDeJuego(), false, true) == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			)
		)
			estariaEnJaque = false;
		else if(
			(char) (letra - 1) >= 'A'
			&&
			numero - 1 >= 1
			&&
			(
				rey.moverA((char) (letra - 1), numero - 1, _juego.getEstadoDeJuego(), false, true) == EnumError.SIN_ERROR
				||
				rey.moverA((char) (letra - 1), numero - 1, _juego.getEstadoDeJuego(), false, true) == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			)
		)
			estariaEnJaque = false;
		else if(
			numero - 1 >= 1
			&&
			(
				rey.moverA(letra, numero - 1, _juego.getEstadoDeJuego(), false, true) == EnumError.SIN_ERROR
				||
				rey.moverA(letra, numero - 1, _juego.getEstadoDeJuego(), false, true) == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			)
		)
			estariaEnJaque = false;
		else if(
			(char) (letra + 1) <= 'H'
			&&
			numero - 1 >= 1
			&&
			(
				rey.moverA((char) (letra + 1), numero - 1, _juego.getEstadoDeJuego(), false, true) == EnumError.SIN_ERROR
				||
				rey.moverA((char) (letra + 1), numero - 1, _juego.getEstadoDeJuego(), false, true) == EnumError.OK_MOVIMIENTO_Y_CAPTURA
			)
		)
			estariaEnJaque = false;
		
		retorno = false;
		if(estariaEnJaque) {
		// El sigue estando en jaque si realiza algún movimiento.
			indice = 0;
			while(estariaEnJaque && indice < 64) {
				if(_casillas[indice] != null && _casillas[indice].getColor() == turno && _casillas[indice].consultarTipoDePieza() != EnumTipoPieza.REY)
				// Es una pieza propia diferente al rey.

					// Esta pieza puede hacer algún movimiento (ya sea de
					// captura o movimiento) que bloquee el camino, hacia el rey,
					// de todas las piezas contrarias que ponen en jaque al rey
					// (se incluye el hecho de que una vez realizado el movimiento
					// el rey propio no quede en jaque por haber desbloqueado el
					// camino a otra pieza contraria). El rey no está en jaque mate.
					if(_casillas[indice].hayAlgunMovimientoValido()) estariaEnJaque = false;
						
				indice++;
			}

			// No se encontró ninguna pieza que cumpla con todas las condiciones
			// anteriores. El rey está en jaque mate.
			if(estariaEnJaque) retorno = true;

			// El rey no está en jaque mate.
			else retorno = false;			
		
		// No está en jaque mate.
		} else retorno = false;
		
		*/

		return true;
	}

	// Devuelve el estado del juego que conoce este tablero.
	private EnumEstadoDeJuego consultarEstadoDelJuego() { return _juego.getEstadoDeJuego(); }
	
	// Funcionamiento: quita todas las piezas del tablero.
	private void quitarPiezas() {
		for(char letra = 'A'; letra <= 'H'; letra++)
			for(int numero = 1; numero <= 8; numero++)
				quitarPiezaPorPosicion(letra, numero);
	}
	
}
