package ajedrez.servidor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumEstadoDeJuego;
import ajedrez.compartido.IControladorServidor;

class ControladorServidor implements IControladorServidor {

    /* Miembros públicos. */

    public ControladorServidor() {
        //_observadores = new HashMap<String, JugadorRegistrado>();
        _socketsClientes = new ArrayList<String>();
        _juego = new Juego();
        _cantJugadoresRegistrados = 0;
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

        return _juego.registrarJugador(nombre, socket);
        
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
        if (_socketsClientes.size() == 2) return EnumError.SALA_LLENA;

        //_Socket nuevoCliente = new _Socket(ipDeCliente, puertoDeCliente);
        for (String socketRegistrado : _socketsClientes) if (socketRegistrado.equals(socket)) return EnumError.SOCKET_DUPLICADO;

        _socketsClientes.add(socket);
        System.out.println("Cliente conectado en socket " + socket + ".");
        return EnumError.SIN_ERROR;
    }

    @Override
	public EnumError iniciarPartida() {

        if (_cantJugadoresRegistrados < 2) return EnumError.FALTAN_JUGADORES;

        if (
            _juego.getEstadoDeJuego() != EnumEstadoDeJuego.SIN_PARTIDA
            &&
            _juego.getEstadoDeJuego() != EnumEstadoDeJuego.SIN_ESTADO
        ) return EnumError.PARTIDA_EN_CURSO;

        return _juego.iniciarPartida();

    }


    /* Miembros privados. */

    //private HashMap<String, JugadorRegistrado> _observadores;
    private ArrayList<String> _socketsClientes;
    private Juego _juego;
    private byte _cantJugadoresRegistrados;

}
