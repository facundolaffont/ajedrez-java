package ajedrez.servidor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import ajedrez.compartido.IControladorServidor;
import ajedrez.compartido.IObservador;
import ajedrez.compartido.Terminador;

class ControladorServidor implements IControladorServidor {

    /* Miembros públicos. */

    public ControladorServidor() {
        _juego = new Juego();
        _observadores = new ArrayList<IObservador>();
    }

    /**
     * Sólo devuelve un valor para verificar que la conexión está en
     * buen estado.
     * 
     * @return 0 - La conexión está en buen estado.
     */
    @Override
    public int verificarConexion() throws RemoteException { return 0; }

    /**
     * @return
     *      0 - OK: jugador registrado;
     *     -1 - Hay una partida en curso;
     *     -2 - No existe la conexión con el socket especificado.
     */
    @Override
    public int registrarJugador(String nombre, String socket) throws RemoteException {
        int codigoError = _juego.registrarJugador(nombre, socket);
        switch(codigoError) {
            case -1: return -2;
            case -2: return -1;
        }

        return 0;
    }

    /**
     * Registra al host como observador si no está llena la sala
     * o si no hay otro socket idéntico registrado.
     *
     * @param ipDeCliente IPv4 del host que se quiere registrar.
     * @param puertoDeCliente Puerto del host que se quiere registrar.
     *
     * @return
     *      0 - Observador registrado;
     *     -1 - Sala llena;
     *     -2 - El socket ya existe;
     *     -3 - Formato de socket no válido;
     *     -4 - Puerto de socket inválido.
     */
    @Override
    public int registrarObservador(String socket) throws RemoteException {
        // Validaciones de formato.
        if(!_formatoDeSocketValido(socket)) return -3;
        if(!_puertoDeSocketValido(socket)) return -4;

        // Intenta registrar el socket.
        int codigoError = _juego.registrarSocket(socket);
        switch(codigoError) {
            case -1: return -2;
            case -2: return -1;
        }

        // Se registra el observador.
        try {
            Registry registroRMI = LocateRegistry.getRegistry(
                _obtenerIPdeSocket(socket),
                _obtenerPuertoDeSocket(socket)
            );
            IObservador observador;
            observador = (IObservador) registroRMI.lookup("ControladorCliente");

            _observadores.add(observador);
            System.out.println("Cliente conectado en socket " + socket + ".");
            return 0;
        } catch (NotBoundException e) {
            Terminador.getInstance().terminarPorExcepcion(e, e.getMessage());
            return -255;
        } catch (Exception e) {
            Terminador.getInstance().terminarPorExcepcion(e, e.getMessage());
            return -255;
        }
    }

    /**
     * @return
     *      0 - Partida iniciada;
     *     -1 - Faltan registrarse jugadores;
     *     -2 - Hay una partida en curso.
     */
    @Override
	public int iniciarPartida() {
        int codigoError = _juego.iniciarPartida();
        switch(codigoError) {
            case -1: return -1;
            case -2: return -2;
        }

        // Partida inicializada.
        //_notificarObservadores( QUÉ ENVÍO );

        return 0;
    }


    /* Miembros privados. */

    private Juego _juego;
    private ArrayList<IObservador> _observadores;

    private boolean _formatoDeSocketValido(String socket) {
        return true;
    }

    private boolean _puertoDeSocketValido(String socket) {
        return true;
    }

    private String _obtenerIPdeSocket(String socket) {
        return null;
    }
    
    private int _obtenerPuertoDeSocket(String socket) {
        return 0;
    }

    private void _notificarObservadores(Object mensaje) {
        for (IObservador observador : _observadores) {
            try { observador.actualizar(mensaje); }
            catch (RemoteException e) {
                Terminador
                    .getInstance()
                    .terminarPorExcepcion(e, e.getMessage()
                );
            }
            catch (Exception e) {
                Terminador
                    .getInstance()
                    .terminarPorExcepcion(e, e.getMessage()
                );
            }
        }
    }
}