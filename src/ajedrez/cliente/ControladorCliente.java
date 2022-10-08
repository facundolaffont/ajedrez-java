package ajedrez.cliente;

import java.rmi.RemoteException;

import ajedrez.cliente.vista.IVista;
import ajedrez.cliente.vista.VistaConsola;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.IControladorServidor;

public class ControladorCliente implements IObservador, IControladorCliente {


    /* Miembros públicos. */
    
    public ControladorCliente() {
        _iControladorServidor = null;

        ClienteAjedrez clienteAjedrez = new ClienteAjedrez(this);
        _jugador = new Jugador(this);
        _iVista = new VistaConsola(this, _jugador, clienteAjedrez);
        ((VistaConsola) _iVista).mostrarVentana();
    }
    
    /**
     * Registra el stub del controlador remoto y le indica que registre este controlador.
     * 
     * @param controladorServidor Stub del controlador remoto.
     * @return
     *    SALA_LLENA - Hay 2 hosts ya registrados;
     *    SOCKET_DUPLICADO - El socket utilizado para conectarse ya está registrado;
     * 		ERROR_DE_COMUNICACION - No se pudo enviar el mensaje al controlador remoto;
     * 		SIN_ERROR.
     */
    public <T extends IControladorServidor> EnumError conectarseAServidor
      (
        T controladorServidor, String ipDeCliente, int puertoDeCliente
      ) {
        _iControladorServidor = (IControladorServidor) controladorServidor;
        try { return _iControladorServidor.registrarObservador(ipDeCliente, puertoDeCliente); }
        catch (RemoteException e) { return EnumError.ERROR_DE_COMUNICACION; }
    }

    /**
     * Verifica el estado de la conexión con el servidor.
     * 
     * @return
     * 		SIN_CONEXION - No existe un servidor conectado, todavía;
     * 		ERROR_DE_COMUNICACION - Hubo un error al intentar comunicarse con el servidor conectado;
     * 		ERROR_DESCONOCIDO - Ocurrió un error no previsto;
     * 		SIN_ERROR - Hay conexión con el servidor.
     */
  public EnumError verificarConexionConServidor() {
    if(_iControladorServidor == null) return EnumError.SIN_CONEXION;

    try {
      EnumError codigoError = _iControladorServidor.verificarConexion();
      switch(codigoError) {
        case SIN_ERROR: return codigoError;
        default: return EnumError.ERROR_DESCONOCIDO;
      }
    }
    catch (RemoteException e) { return EnumError.ERROR_DE_COMUNICACION; }
  }

    /**
     * Registra un jugador en el servidor.
     * 
     * @param nombre Nombre del jugador que se desea registrar.
     * @return
     * 		ERROR_DE_COMUNICACION (error al intentar enviar el mensaje al servidor);
     *		PARTIDA_EN_CURSO;
     *		SALA_LLENA;
     *		SIN_ERROR
     */
  public EnumError registrarJugador(String nombre) {
      try { return _iControladorServidor.registrarJugador(nombre); }
      catch (RemoteException e) { return EnumError.ERROR_DE_COMUNICACION; }
  }

  /**
   * Método llamado por el controlador del servidor para notificar las novedades.
   */
  @Override
  public void actualizar(Object mensaje) {}


  /* Miembros privados. */

  private IControladorServidor _iControladorServidor;
  private Jugador _jugador;
  private IVista _iVista;

}
