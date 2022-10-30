package ajedrez.cliente;

import java.rmi.RemoteException;
import ajedrez.cliente.vista.IVista;
import ajedrez.cliente.vista.VistaConsola;
import ajedrez.compartido.IControladorServidor;

class ControladorCliente implements IObservador, IControladorCliente {

    /* Miembros públicos. */
    
    public ControladorCliente() {
        _iControladorServidor = null;

        _clienteAjedrez = new ClienteAjedrez(this);
        _jugador = new Jugador(this);
        _iVista = new VistaConsola(this, _jugador, _clienteAjedrez);
        ((VistaConsola) _iVista).mostrarVentana();
    }
    
    /**
     * Registra el stub del controlador remoto y le indica que registre este controlador.
     * 
     * @param controladorServidor Stub del controlador remoto.
     * @return
     *      0 - Se estableció la conexión y se registró el observador;
     *      -1 - La sala está llena;
     *      -2 - El socket utilizado para conectarse ya está registrado;
     *      -3 - No se pudo enviar el mensaje al servidor.
     */
    public <T extends IControladorServidor> int conectarseAServidor
        (
            T controladorServidor, String socket
        ) {
            _iControladorServidor = (IControladorServidor) controladorServidor;
            try {
                int codigoError = _iControladorServidor.registrarObservador(socket);

                switch(codigoError) {
                    case -1: return -1;
                    case -2: return -2;
                }

                return 0;
            }
            catch (RemoteException e) { return -3; }
    }

    /**
     * Verifica el estado de la conexión con el servidor.
     * 
     * @return
     * 		0 - Hay conexión con el servidor;
     *      -1 - No existe conexión con un servidor;
     * 		-2 - Hubo un error al intentar comunicarse con el servidor.
     */
    public int verificarConexionConServidor() {
        if(_iControladorServidor == null) return -1;

        try {
            _iControladorServidor.verificarConexion();
            
            return 0;
        }
        catch (RemoteException e) { return -2; }
    }

    /**
     * Registra un jugador en el servidor.
     * 
     * @param nombre Nombre del jugador que se desea registrar.
     * @return
     *		0 - Nombre registrado;
     *      -1 - Todavía no se estableció conexión con el servidor;
     * 		-2 - Error de comunicación al enviar el mensaje al servidor;
     *		-3 - Hay una partida en curso;
     *      -4 - No existe conexión con el socket especificado.
     */
    public int registrarJugador(String nombre) {
        if (_iControladorServidor == null) return -1;

        try {
            int codigoError = _iControladorServidor.registrarJugador(
                nombre,
                _clienteAjedrez.getSocket()
            );
            
            switch(codigoError) {
                case -1: return -3;
                case -2: return -4;
            }

            return 0;
        }
        catch (RemoteException e) { return -2; }
    }

    /**
     * Método llamado por el controlador del servidor para notificar las novedades.
     */
    @Override
    public void actualizar(Object mensaje) {}

    /**
     * @return
     *      0 - Partida iniciada;
     *      -1 - Todavía no se estableció conexión con el servidor;
     *      -2 - El jugador todavía no tiene especificado un nombre;
     *      -3 - Error de comunicación al intentar conectarse con el servidor.
     */
    @Override
    public int iniciarPartida() {
        if (!_clienteAjedrez.hayConexionConServidor()) return -1;
        if (_jugador.getNombre() == null) return -2;

        try {
            _iControladorServidor.iniciarPartida();
            return 0;
        }
        catch (Exception e) { return -3; }
    }


    /* Miembros privados. */

    private ClienteAjedrez _clienteAjedrez;
    private IControladorServidor _iControladorServidor;
    private Jugador _jugador;
    private IVista _iVista;
}
