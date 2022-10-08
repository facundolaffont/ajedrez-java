package ajedrez.cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.IControladorServidor;

public class ClienteAjedrez implements IClienteAjedrez {
	

	/* Miembros públicos */

	public ClienteAjedrez(ControladorCliente controlador) {
		_controlador = controlador;
		_ipCliente = null;
		_puertoCliente = 0;
		_ipServidor = null;
		_puertoServidor = 0;
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
	 * 		ERROR_DESCONOCIDO - Cualquier otro error no contemplado en el diseño;
	 * 		SALA_LLENA - Hay 2 jugadores conectados actualmente;
   * 		SOCKET_DUPLICADO - El socket utilizado para conectarse ya está registrado;
	 * 		SIN_ERROR - Conexión exitosa.
	 */
	@Override
	public EnumError conectarseAServidor(String ipServidor, int puertoServidor) {

		// Verifica que el cliente esté configurado.
		if(_ipCliente == null) return EnumError.CLIENTE_SIN_CONFIGURAR;

		// Valida el IP y puerto del servidor.
		if(ipServidor == null) return EnumError.VALOR_INVALIDO;
		if(puertoServidor < 1025 || puertoServidor > 65535) return EnumError.VALOR_INVALIDO;
		
		// Verifica que no esté actualmente conectado a un servidor.
		if(_ipServidor != null) return EnumError.CONEXION_EXISTENTE;

        try {
            Registry registroRMI = LocateRegistry.getRegistry(ipServidor, puertoServidor);
            _controladorStub = (IControladorServidor) registroRMI.lookup("ControladorServidor");

			// Conexión exitosa, se deja registro de los datos del servidor.
			_ipServidor = ipServidor;
			_puertoServidor = puertoServidor;
			return _controlador.conectarseAServidor(_controladorStub, _ipCliente, _puertoCliente);
        } catch (Exception e) { return EnumError.ERROR_DE_COMUNICACION; }

	}

	/**
	 * Configura los parámetros del cliente, si no está conectado a un servidor.
	 * 
	 * @param ipCliente IP del cliente.
	 * @param puertoCliente Puerto del cliente.
	 * @return
	 * 		SIN_ERROR - Configuración exitosa;
	 * 		VALOR_NULO - ipCliente es nulo;
	 * 		VALOR_INVALIDO - puertoCliente es menor a 1025 o mayor a 65535;
	 * 		CONEXION_EXISTENTE - Hay una conexión en curso, y no pueden cambiarse los parámetros de conexión.
	 */
	@Override
	public EnumError configurarCliente(String ipCliente, int puertoCliente) {
		if(_ipServidor != null) { return EnumError.CONEXION_EXISTENTE; }
		if(ipCliente == null) { return EnumError.VALOR_NULO; }
		if(puertoCliente < 1025 || puertoCliente > 65535) { return EnumError.VALOR_INVALIDO; }
		
		_ipCliente = ipCliente;
		_puertoCliente = puertoCliente;
		return EnumError.SIN_ERROR;
	}

	/**
	 * Verifica si hay conexion con el servidor.
	 * 
	 * @return
	 * 		SIN_CONEXION - No existe un servidor conectado, todavía;
	 * 		ERROR_DE_COMUNICACION - Hubo un error al intentar comunicarse con el servidor conectado;
	 * 		ERROR_DESCONOCIDO - Ocurrió un error no previsto;
	 * 		SIN_ERROR - Hay conexión con el servidor.
	 */
	@Override
	public EnumError chequearConexionConServidor() {
		EnumError codigoError = _controlador.verificarConexionConServidor();
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
	private String _ipServidor;
	private int _puertoServidor;
	private ControladorCliente _controlador;
	private IControladorServidor _controladorStub;
	
}
