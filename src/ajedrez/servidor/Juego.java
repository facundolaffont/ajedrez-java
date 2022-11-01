package ajedrez.servidor;

import java.util.HashMap;

import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.EnumEstadoDeJuego;

class Juego {

    /* Miembros públicos. */

    public Juego() {
        _jugadoresRegistrados = new HashMap<String, JugadorRegistrado>();
        _estadoDelJuego = EnumEstadoDeJuego.SIN_PARTIDA;
        _piezas = new Pieza[32];
        _tablero = new Tablero(this);
        _turno = EnumColorPieza.BLANCA;
    }

    public EnumEstadoDeJuego getEstadoDeJuego() {
        return _estadoDelJuego;
    }

    public void setEstadoDeJuego(EnumEstadoDeJuego estadoDelJuego) {
        _estadoDelJuego = estadoDelJuego;
    }

    /**
     * @return
     *      0 - Se inicializó la partida;
     *      -1 - Faltan jugadores;
     *      -2 - Hay una partida en curso;
     */
    public int iniciarPartida() {
        if(!salaLlena()) return -1;

        if(_estadoDelJuego != EnumEstadoDeJuego.SIN_PARTIDA) return -2;

        // Se crean las piezas blancas.
        _piezas[0] = new Torre(EnumColorPieza.BLANCA);
        _piezas[1] = new Caballo(EnumColorPieza.BLANCA);
        _piezas[2] = new Alfil(EnumColorPieza.BLANCA);
        _piezas[3] = new Dama(EnumColorPieza.BLANCA);
        _piezas[4] = new Rey(EnumColorPieza.BLANCA);
        _piezas[5] = new Alfil(EnumColorPieza.BLANCA);
        _piezas[6] = new Caballo(EnumColorPieza.BLANCA);
        _piezas[7] = new Torre(EnumColorPieza.BLANCA);
        _piezas[8] = new Peon(EnumColorPieza.BLANCA);
        _piezas[9] = new Peon(EnumColorPieza.BLANCA);
        _piezas[10] = new Peon(EnumColorPieza.BLANCA);
        _piezas[11] = new Peon(EnumColorPieza.BLANCA);
        _piezas[12] = new Peon(EnumColorPieza.BLANCA);
        _piezas[13] = new Peon(EnumColorPieza.BLANCA);
        _piezas[14] = new Peon(EnumColorPieza.BLANCA);
        _piezas[15] = new Peon(EnumColorPieza.BLANCA);
        
        // Se crean las piezas negras.
        _piezas[16] = new Peon(EnumColorPieza.NEGRA);
        _piezas[17] = new Peon(EnumColorPieza.NEGRA);
        _piezas[18] = new Peon(EnumColorPieza.NEGRA);
        _piezas[19] = new Peon(EnumColorPieza.NEGRA);
        _piezas[20] = new Peon(EnumColorPieza.NEGRA);
        _piezas[21] = new Peon(EnumColorPieza.NEGRA);
        _piezas[22] = new Peon(EnumColorPieza.NEGRA);
        _piezas[23] = new Peon(EnumColorPieza.NEGRA);
        _piezas[24] = new Torre(EnumColorPieza.NEGRA);
        _piezas[25] = new Caballo(EnumColorPieza.NEGRA);
        _piezas[26] = new Alfil(EnumColorPieza.NEGRA);
        _piezas[27] = new Dama(EnumColorPieza.NEGRA);
        _piezas[28] = new Rey(EnumColorPieza.NEGRA);
        _piezas[29] = new Alfil(EnumColorPieza.NEGRA);
        _piezas[30] = new Caballo(EnumColorPieza.NEGRA);
        _piezas[31] = new Torre(EnumColorPieza.NEGRA);
        
        // Limpia el tablero.
        _tablero.quitarPiezas();
        
        // Se establecen las piezas en sus respectivas posiciones.
        // Utilizar la siguiente referencia:
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
        char letra = 'A';
        for(int i = 0; i <= 7; i++) {
            _tablero.colocarPieza(_piezas[i], letra++, 1);
            _piezas[i].setTablero(_tablero);
        }
        letra = 'A';
        for(int i = 8; i <= 15; i++) {
            _tablero.colocarPieza(_piezas[i], letra++, 2);
            _piezas[i].setTablero(_tablero);
        }
        letra = 'A';
        for(int i = 16; i <= 23; i++) {
            _tablero.colocarPieza(_piezas[i], letra++, 7);
            _piezas[i].setTablero(_tablero);
        }
        letra = 'A';
        for(int i = 24; i <= 31; i++) {
            _tablero.colocarPieza(_piezas[i], letra++, 8);
            _piezas[i].setTablero(_tablero);
        }
        
        // Se configura el estado del juego.
        _estadoDelJuego = EnumEstadoDeJuego.PARTIDA_ACTIVA_EN_JUEGO;
        _turno = EnumColorPieza.BLANCA;

        return 0;

    }

    /**
     * 
     * @param nombre
     * @param socket
     * @return
     *      0 - Se registró el jugador;
     *      -1 - No hay conexión establecida con el socket especificado;
     *      -2 - Partida en curso.
     */
    public int registrarJugador(String nombre, String socket) {
        if (_estadoDelJuego != EnumEstadoDeJuego.SIN_PARTIDA) return -2;
        
        if (!_existeElSocket(socket)) return -1;

        _jugadoresRegistrados
            .get(socket)
            .setNombre(nombre);
        return 0;
    }

    /**
     * 
     * @param socket
     * @return
     *      0 - Se registró el socket;
     *      -1 - El socket ya está registrado;
     *      -2 - Sala llena.
     */
    public int registrarSocket(String socket) {
        if (salaLlena()) return -2;

        if(_jugadoresRegistrados.containsKey(socket)) return -1;

        _jugadoresRegistrados.put(socket, new JugadorRegistrado());
        return 0;
    }

    public boolean salaLlena() {
        if (_jugadoresRegistrados.size() == 2) return true;
        return false;
    }


    /* Miembros privados. */

    private HashMap<String, JugadorRegistrado> _jugadoresRegistrados;
    private EnumEstadoDeJuego _estadoDelJuego;
    private Pieza[] _piezas;
    private Tablero _tablero;
    private EnumColorPieza _turno;
    
    private boolean _existeElSocket(String socket) {
        if (!_jugadoresRegistrados.containsKey(socket)) return false;

        return true;
    }

}
