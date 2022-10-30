package ajedrez.servidor;

import java.util.HashMap;
import ajedrez.compartido.EnumEstadoDeJuego;

class Juego {

    /* Miembros públicos. */

    public Juego() {
        _jugadoresRegistrados = new HashMap<String, JugadorRegistrado>();
        _estadoDelJuego = EnumEstadoDeJuego.SIN_PARTIDA;
    }

    public EnumEstadoDeJuego getEstadoDeJuego() {
        return _estadoDelJuego;
    }

    public void setEstadoDeJuego(EnumEstadoDeJuego estadoDelJuego) {
        _estadoDelJuego = estadoDelJuego;
    }

    /**
     * 
     * @return
     *      0 - Se inicializó la partida.
     */
    public int iniciarPartida() {

        /*
        if(!salaLlena()) return EnumError.FALTAN_JUGADORES;

        if(estadoDelJuego != EnumEstadoDeJuego.SIN_PARTIDA) return EnumError.ESTADO_DE_PARTIDA_INCORRECTO;

        // Se crean las piezas blancas.
        piezas[0] = new Torre(EnumColorPieza.BLANCA);
        piezas[1] = new Caballo(EnumColorPieza.BLANCA);
        piezas[2] = new Alfil(EnumColorPieza.BLANCA);
        piezas[3] = new Dama(EnumColorPieza.BLANCA);
        piezas[4] = new Rey(EnumColorPieza.BLANCA);
        piezas[5] = new Alfil(EnumColorPieza.BLANCA);
        piezas[6] = new Caballo(EnumColorPieza.BLANCA);
        piezas[7] = new Torre(EnumColorPieza.BLANCA);
        piezas[8] = new Peon(EnumColorPieza.BLANCA);
        piezas[9] = new Peon(EnumColorPieza.BLANCA);
        piezas[10] = new Peon(EnumColorPieza.BLANCA);
        piezas[11] = new Peon(EnumColorPieza.BLANCA);
        piezas[12] = new Peon(EnumColorPieza.BLANCA);
        piezas[13] = new Peon(EnumColorPieza.BLANCA);
        piezas[14] = new Peon(EnumColorPieza.BLANCA);
        piezas[15] = new Peon(EnumColorPieza.BLANCA);
        
        // Se crean las piezas negras.
        piezas[16] = new Peon(EnumColorPieza.NEGRA);
        piezas[17] = new Peon(EnumColorPieza.NEGRA);
        piezas[18] = new Peon(EnumColorPieza.NEGRA);
        piezas[19] = new Peon(EnumColorPieza.NEGRA);
        piezas[20] = new Peon(EnumColorPieza.NEGRA);
        piezas[21] = new Peon(EnumColorPieza.NEGRA);
        piezas[22] = new Peon(EnumColorPieza.NEGRA);
        piezas[23] = new Peon(EnumColorPieza.NEGRA);
        piezas[24] = new Torre(EnumColorPieza.NEGRA);
        piezas[25] = new Caballo(EnumColorPieza.NEGRA);
        piezas[26] = new Alfil(EnumColorPieza.NEGRA);
        piezas[27] = new Dama(EnumColorPieza.NEGRA);
        piezas[28] = new Rey(EnumColorPieza.NEGRA);
        piezas[29] = new Alfil(EnumColorPieza.NEGRA);
        piezas[30] = new Caballo(EnumColorPieza.NEGRA);
        piezas[31] = new Torre(EnumColorPieza.NEGRA);
        
        // Limpia el tablero.
        tablero.quitarPiezas();
        
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
            tablero.colocarPieza(piezas[i], letra++, 1);
            piezas[i].setTablero(tablero);
        }
        letra = 'A';
        for(int i = 8; i <= 15; i++) {
            tablero.colocarPieza(piezas[i], letra++, 2);
            piezas[i].setTablero(tablero);
        }
        letra = 'A';
        for(int i = 16; i <= 23; i++) {
            tablero.colocarPieza(piezas[i], letra++, 7);
            piezas[i].setTablero(tablero);
        }
        letra = 'A';
        for(int i = 24; i <= 31; i++) {
            tablero.colocarPieza(piezas[i], letra++, 8);
            piezas[i].setTablero(tablero);
        }
        
        // Se configura el estado del juego.
        estadoDelJuego = EnumEstadoDeJuego.PARTIDA_ACTIVA_EN_JUEGO;
        turno = EnumColorPieza.BLANCA;
        piezaSeleccionada = null;
         
		// Indicamos que la notificación conlleva información
		// sobre el inicio de partida.	
		informacion.reestablecerValores();
		informacion.setInicioDePartida(true);
        
        // Se configura la información que será notificada.
        informacion.setError(EnumError.SIN_ERROR);

		// Notifica a los observadores que el juego inició.
		informacion.setEstadoDelJuego(estadoDelJuego);
		informacion.setTablero(tablero.obtenerInfoDeTablero());
		informacion.setTurnoActual(turno);
		try { this.notificarObservadores(informacion); }
        catch (RemoteException e) { e.printStackTrace(); }

        */

        return 0; // CAMBIAR.

    }

    /**
     * 
     * @param nombre
     * @param socket
     * @return
     *      0 - Se registró el jugador;
     *      -1 - No hay conexión establecida con el socket especificado.
     */
    public int registrarJugador(String nombre, String socket) {
        if (!_existeElSocket(socket)) return -1; // TODO: #8 cambiar todos los retornos por valores enteros, e independizar métodos de los retornos de sus llamadas.

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
     *      -1 - El socket ya está registrado.
     */
    public int registrarSocket(String socket) {
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
    
    private boolean _existeElSocket(String socket) {
        if (!_jugadoresRegistrados.containsKey(socket)) return false;

        return true;
    }

}
