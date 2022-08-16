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
		this._iControladorServidor = (IControladorServidor) controladorServidor;
	}

    public EnumError pingAlServidor() {
		if(_iControladorServidor == null) return EnumError.SIN_CONEXION;

		try {
			EnumError codigoError = _iControladorServidor.ping();
			switch(codigoError) {
				case SIN_ERROR:
					return codigoError;
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