package ajedrez.controlador;

import java.rmi.RemoteException;

import ajedrez.cliente.ClienteAjedrez;
import ajedrez.cliente.Jugador;
import ajedrez.modelo.EnumError;
import ajedrez.servidor.IControladorServidor;
import ajedrez.vista.IVista;
import ajedrez.vista.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class ControladorCliente implements IControladorRemoto, IControladorCliente {


	/* Miembros públicos. */
	
	public ControladorCliente() {
		_iControladorServidor = null;

		ClienteAjedrez clienteAjedrez = new ClienteAjedrez(this);
		_jugador = new Jugador(this);
		_iVista = new VistaConsola(this, _jugador, clienteAjedrez);
		((VistaConsola) _iVista).mostrarVentana();
	}
	
	@Override
	public void actualizar(IObservableRemoto controladorServidor, Object mensaje) {}

	public <T extends IObservableRemoto> void setModeloRemoto(T controladorServidor) {
		_iControladorServidor = (IControladorServidor) controladorServidor;
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
    public EnumError chequearConexionConServidor() {
		if(_iControladorServidor == null) return EnumError.SIN_CONEXION;

		try {
			EnumError codigoError = _iControladorServidor.chequearConexion();
			switch(codigoError) {
				case SIN_ERROR: return codigoError;
				default: return EnumError.ERROR_DESCONOCIDO;
			}
		}
		catch (RemoteException e) { return EnumError.ERROR_DE_COMUNICACION; }
    }


	/* Miembros privados. */

	private IControladorServidor _iControladorServidor;
	private Jugador _jugador;
	private IVista _iVista;

}