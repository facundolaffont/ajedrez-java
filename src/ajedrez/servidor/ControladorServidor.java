package ajedrez.servidor;

import java.rmi.RemoteException;
import java.util.HashMap;
import ajedrez.modelo.EnumError;

public class ControladorServidor implements IControladorServidor {


    /* Miembros públicos. */

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
    public EnumError registrarJugador(String nombre) throws RemoteException {
        /* Si la sala está llena -> PARTIDA_EN_CURSO
         *
         * Si no hay espacio en la sala -> SALA_LLENA
         * 
         */

        /* Armar una estructura que mapee el ID del objeto que llama a un jugador.
         * Crearía un jugador nuevo y lo guardaría en ese lugar, sin importar
         * si ya hay un objeto ahí o no porque dicha referencia, al perderse,
         * se limpiaría gracias al colector de basura.
         * 
         * También debería crear un objeto Juego, y el controlador debería
         * conocerlo y crearlo ni bien se crea el controlador.
         */

        return EnumError.SIN_ERROR; // El jugador pudo ser registrado.
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
    public EnumError registrarObservador(String ipDeCliente, int puertoDeCliente) throws RemoteException {
      if (_observadores.size() == 2) return EnumError.SALA_LLENA;

      Socket nuevoCliente = new Socket(ipDeCliente, puertoDeCliente);
      if (_observadores.containsKey(nuevoCliente)) return EnumError.SOCKET_DUPLICADO;

      _observadores.put(nuevoCliente, null);
      System.out.println("Cliente conectado en socket <" + nuevoCliente.ip + ":" + nuevoCliente.puerto + ">");
      return EnumError.SIN_ERROR;
    }


    /* Miembros privados. */
    private HashMap<Socket, String> _observadores = new HashMap<Socket, String>();

    private class Socket {
        public String ip;
        public int puerto;

        public Socket(String ip, int puerto) {
            this.ip = ip;
            this.puerto = puerto;
        }
    }

}
