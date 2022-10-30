package ajedrez.servidor;

import java.rmi.RemoteException;
import ajedrez.compartido.EnumEstadoDeJuego;
import ajedrez.compartido.IControladorServidor;

class ControladorServidor implements IControladorServidor {

    /* Miembros públicos. */

    public ControladorServidor() {
        _juego = new Juego();
        _cantJugadoresRegistrados = 0;
    }

    /**
     * Sólo devuelve un valor para verificar que la conexión está en
     * buen estado.
     * 
     * @return 0 - La conexión está en buen estado.
     */
    @Override
    public int verificarConexion() throws RemoteException {
        return 0;
    }

    /**
     * @return
     *      0 - OK: jugador registrado;
     *      -1 - Hay una partida en curso;
     *      -2 - No existe la conexión con el socket especificado.
     */
    @Override
    public int registrarJugador(String nombre, String socket) throws RemoteException {
        if (_juego.getEstadoDeJuego() != EnumEstadoDeJuego.SIN_PARTIDA)
            return -1;

        int codigoError = _juego.registrarJugador(nombre, socket);

        if (codigoError == -1) return -2;
        else return 0;
    }

    /**
     * Registra al host como observador si no está llena la sala
     * o si no hay otro socket idéntico registrado.
     *
     * @param ipDeCliente IPv4 del host que se quiere registrar.
     * @param puertoDeCliente Puerto del host que se quiere registrar.
     *
     * @return
     *    0 - Observador registrado;
     *    -1 - Sala llena;
     *    -2 - El socket ya existe.
     */
    @Override
    public int registrarObservador(String socket) throws RemoteException {
        if (_juego.salaLlena()) return -1;

        if(_juego.registrarSocket(socket) == -1) return -2;

        System.out.println("Cliente conectado en socket " + socket + ".");
        return 0;
    }

    /**
     * @return
     *      0 - Partida iniciada;
     *      -1 - Faltan registrarse jugadores;
     *      -2 - Hay una partida en curso.
     */
    @Override
	public int iniciarPartida() {
        if (_cantJugadoresRegistrados < 2) return -1;

        if (
            _juego.getEstadoDeJuego() != EnumEstadoDeJuego.SIN_PARTIDA
            &&
            _juego.getEstadoDeJuego() != EnumEstadoDeJuego.SIN_ESTADO
        ) return -2;

        int codigoError = _juego.iniciarPartida();
        switch(codigoError) {
            //..
        }

        return 0;
    }


    /* Miembros privados. */

    private Juego _juego;
    private byte _cantJugadoresRegistrados;
}