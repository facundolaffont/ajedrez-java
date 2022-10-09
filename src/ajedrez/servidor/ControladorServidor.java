package ajedrez.servidor;

import java.rmi.RemoteException;
import java.util.HashMap;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumEstadoDeJuego;
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
      System.out.println("Cliente conectado en socket " + socket);
      return EnumError.SIN_ERROR;
    }


    /* Miembros privados. */
    private HashMap<String, JugadorRegistrado> _observadores = new HashMap<String, JugadorRegistrado>();
    private Juego _juego;

    /*
    private class _Socket {
        public String ip;
        public int puerto;

        public _Socket(String ip, int puerto) {
            this.ip = ip;
            this.puerto = puerto;
        }
    }
    */

}
