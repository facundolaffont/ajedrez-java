package ajedrez.cliente;

import java.rmi.RemoteException;

import ajedrez.controlador.ControladorCliente;
import ajedrez.modelo.EnumError;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class ClienteAjedrez implements IClienteAjedrez {
	

	/* Miembros públicos */

	public ClienteAjedrez(ControladorCliente controlador) {
		_controlador = controlador;
		_ipCliente = null;
		_puertoCliente = 0;
		_clienteRMIMVC = null;
	}

	/**
	 * Conecta al cliente con el servidor.
	 * 
	 * @param ipServidor IP del servidor al que se quiere conectar.
	 * @param puertoServidor Puerto del servidor al que se quiere conectar.
	 * @return 
	 *		CLIENTE_SIN_CONFIGURAR - Falta configurar el cliente;
	 * 		VALOR_INVALIDO - ipServidor es nulo, o puertoServidor es menor a 1025 o mayor a 65535;
	 * 		CONEXION_EXISTENTE - Ya existe una conexión con un servidor.
	 * 		ERROR_DE_COMUNICACION - Error de red al intentar conectarse con el servidor;
	 * 		ERROR_DE_RMI - Error del módulo RMI-MVC;
	 * 		ERROR_DESCONOCIDO - Cualquier otro error no contemplado en el diseño;
	 * 		SIN_ERROR - Conexión exitosa.
	 */
	public EnumError conectarseAServidor(String ipServidor, int puertoServidor) {

		// Verifica que el cliente esté configurado.
		if(_ipCliente == null) return EnumError.CLIENTE_SIN_CONFIGURAR;

		// Valida el IP y puerto del servidor.
		if(ipServidor == null) return EnumError.VALOR_INVALIDO;
		if(puertoServidor < 1025 || puertoServidor > 65535) return EnumError.VALOR_INVALIDO;
		
		// Verifica que no esté actualmente conectado a un servidor.
		if(_clienteRMIMVC != null) return EnumError.CONEXION_EXISTENTE;

		// Crea al cliente RMI-MVC.
		_clienteRMIMVC = new Cliente(_ipCliente, _puertoCliente, ipServidor, puertoServidor);

		// Intenta conectarse con el servidor.
		boolean ocurrioExcepcion = false;
		try { _clienteRMIMVC.iniciar(_controlador); }
		catch (RemoteException e) { ocurrioExcepcion = true; return EnumError.ERROR_DE_COMUNICACION; }
		catch (RMIMVCException e) { ocurrioExcepcion = true; return EnumError.ERROR_DE_RMI; }
		catch (Exception e) { ocurrioExcepcion = true; return EnumError.ERROR_DESCONOCIDO; }
		finally { if(ocurrioExcepcion) _clienteRMIMVC = null; }

		return EnumError.SIN_ERROR;
	}

	/**
	 * Configura los parámetros del cliente, si no está conectado a un servidor.
	 * 
	 * @param ipCliente IP del cliente.
	 * @param puertoCliente Puerto del cliente.
	 * @return
	 * 		SIN_ERROR - Configuración exitosa;
	 * 		VALOR_NULO - ipCliente es nulo;
	 * 		VALOR_INVALIDO - puertoCliente es menor a 1 o mayor a 65535;
	 * 		CONEXION_EXISTENTE - Hay una conexión en curso, y no pueden cambiarse los parámetros de conexión.
	 */
	public EnumError configurarCliente(String ipCliente, int puertoCliente) {
		if(_clienteRMIMVC != null) { return EnumError.CONEXION_EXISTENTE; }
		if(ipCliente == null) { return EnumError.VALOR_NULO; }
		if(puertoCliente < 1 || puertoCliente > 65535) { return EnumError.VALOR_INVALIDO; }
		
		_ipCliente = ipCliente;
		_puertoCliente = puertoCliente;
		return EnumError.SIN_ERROR;
	}

	public EnumError enviarPingAlServidor() {
		EnumError codigoError = _controlador.pingAlServidor();
		switch(codigoError) {
			case SIN_ERROR:
			case SIN_CONEXION:
			case ERROR_DE_COMUNICACION:
				return codigoError;
			default: return EnumError.ERROR_DESCONOCIDO;
		}
	}


	/* Miembros privados. */

	private String _ipCliente;
	private int _puertoCliente;
	private Cliente _clienteRMIMVC;
	private ControladorCliente _controlador;
	
}