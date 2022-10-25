package ajedrez.servidor;

import java.rmi.RemoteException;
import java.util.HashMap;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumEstadoDeJuego;
import ajedrez.compartido.EnumColorPieza;
import ajedrez.compartido.IControladorServidor;

class ControladorServidor implements IControladorServidor {

    /* Miembros públicos. */

    public ControladorServidor() {
        _juego = new Juego();
    }

    /**
     * Sólo devuelve un valor para verificar que la conexión está en
     * buen estado.
     * 
     * @return SIN_ERROR - La conexión está en buen estado.
     */
    @Override
    public EnumError verificarConexion() throws RemoteException {
        return EnumError.SIN_ERROR;
    }

    @Override
    public EnumError registrarJugador(String nombre, String socket) throws RemoteException {
        if (_juego.getEstadoDeJuego() != EnumEstadoDeJuego.SIN_PARTIDA)
            return EnumError.PARTIDA_EN_CURSO;

        _observadores
            .get(socket)
            .setNombre(nombre);

        return EnumError.SIN_ERROR;
    }

    /**
     * Registra al host como observador si no está llena la sala
     * o si no hay otro socket idéntico registrado.
     *
     * @param ipDeCliente IPv4 del host que se quiere registrar.
     * @param puertoDeCliente Puerto del host que se quiere registrar.
     *
     * @return
     *    SALA_LLENA - Hay 2 hosts conectados;
     *    SOCKET_DUPLICADO - Se intentó conectar con un host que ya existe;
     *    SIN_ERROR;
     */
    @Override
    public EnumError registrarObservador(String socket) throws RemoteException {
        if (_observadores.size() == 2) return EnumError.SALA_LLENA;

        //_Socket nuevoCliente = new _Socket(ipDeCliente, puertoDeCliente);
        if (_observadores.containsKey(socket)) return EnumError.SOCKET_DUPLICADO;

        _observadores.put(socket, new JugadorRegistrado());
        System.out.println("Cliente conectado en socket " + socket + ".");
        return EnumError.SIN_ERROR;
    }

    @Override
	public EnumError iniciarPartida() {

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
		try {
			this.notificarObservadores(informacion);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

    /* Miembros privados. */

    private HashMap<String, JugadorRegistrado> _observadores = new HashMap<String, JugadorRegistrado>();
    private Juego _juego;

}
