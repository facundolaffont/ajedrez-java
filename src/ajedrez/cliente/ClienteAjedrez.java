package ajedrez.cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import ajedrez.compartido.IControladorServidor;

class ClienteAjedrez implements IClienteAjedrez {

	/* Miembros públicos */

	public ClienteAjedrez(ControladorCliente controlador) {
		_controlador = controlador;
		_ipCliente = null;
		_puertoCliente = 0;
		_ipServidor = null;
		_puertoServidor = 0;
	}

    public boolean hayConexionConServidor() { return _ipServidor == null ? false : true; }

	/**
	 * Conecta al cliente con el servidor.
	 * 
	 * @param ipServidor IP del servidor al que se quiere conectar.
	 * @param puertoServidor Puerto del servidor al que se quiere conectar.
	 * @return 
	 * 		0 - Conexión exitosa;
	 *		-1 - Falta configurar el cliente;
	 * 		-2 - ipServidor es nulo, o puertoServidor es menor a 1024 o mayor a 65535;
	 * 		-3 - Ya existe una conexión con un servidor.
	 * 		-4 - Error de red al intentar conectarse con el servidor;
	 * 		-5 - Cualquier otro error no contemplado en el diseño;
	 * 		-6 - La sala está llena;
     * 		-7 - El socket utilizado para conectarse ya está registrado.
	 */
	@Override
	public int conectarseAServidor(String ipServidor, int puertoServidor) {
		// Verifica que el cliente esté configurado.
		if(_ipCliente == null) return -1;

		// Valida el IP y puerto del servidor.
		if(ipServidor == null) return -2;
		if(puertoServidor < 1024 || puertoServidor > 65535) return -2;
		
		// Verifica que no esté actualmente conectado a un servidor.
		if(_ipServidor != null) return -3;

        try {
            Registry registroRMI = LocateRegistry.getRegistry(ipServidor, puertoServidor);
            _controladorStub = (IControladorServidor) registroRMI.lookup("ControladorServidor");

			// Se intenta conectar.
			_ipServidor = ipServidor;
			_puertoServidor = puertoServidor;
			int codigoError = _controlador.conectarseAServidor(_controladorStub, "<" +_ipCliente + ":" + _puertoCliente + ">");

			// Verifica resultado de la conexión y devuelve el correspondiente código de error.
			if (codigoError == 0) return codigoError;
			else {
				_ipServidor = null;
				_puertoServidor = 0;
				switch(codigoError) {
					case -1: return -6;
					case -2: return -7;
					case -3: return -4;
				}
			}

			return 0;
        } catch (Exception e) { return -4; }
	}

	/**
	 * Configura los parámetros del cliente, si no está conectado a un servidor.
	 * 
	 * @param ipCliente IP del cliente.
	 * @param puertoCliente Puerto del cliente.
	 * @return
	 * 		0 - Configuración exitosa;
	 * 		-1 - ipCliente es nulo;
	 * 		-2 - puertoCliente es menor a 1024 o mayor a 65535;
	 * 		-3 - Hay una conexión en curso y no pueden cambiarse los parámetros de conexión.
	 */
	@Override
	public int configurarCliente(String ipCliente, int puertoCliente) {
		if(_ipServidor != null) { return -3; }
		if(ipCliente == null) { return -1; }
		if(puertoCliente < 1024 || puertoCliente > 65535) { return -2; }
		
		_ipCliente = ipCliente;
		_puertoCliente = puertoCliente;
		return 0;
	}

	/**
	 * Verifica si hay conexion con el servidor.
	 * 
	 * @return
	 * 		0 - Hay conexión con el servidor;
	 * 		-1 - No hay conexión con el servidor;
	 * 		-2 - Hubo un error al intentar comunicarse con el servidor;
	 * 		-3 - Ocurrió un error no previsto.
	 */
	@Override
	public int chequearConexionConServidor() {
		try {
			int codigoError = _controlador.verificarConexionConServidor();

			switch(codigoError) {
				case -1: return -1;
				case -2: return -2;
			}

			return 0;
		} catch (Exception e) { return -3; }
	}

	public String getSocket() {
		return "<" + _ipCliente + ":" + _puertoCliente + ">";
	}


	/* Miembros privados. */

	private String _ipCliente;
	private int _puertoCliente;
	private String _ipServidor;
	private int _puertoServidor;
	private ControladorCliente _controlador;
	private IControladorServidor _controladorStub;
}