package ajedrez.cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import ajedrez.compartido.IControladorServidor;
import ajedrez.compartido.IObservador;
import ajedrez.compartido.Terminador;

class ClienteAjedrez implements IClienteAjedrez {

	/* Miembros públicos */

	public ClienteAjedrez(ControladorCliente controlador) {
		_controladorCliente = controlador;
		_ipHost = null;
		_puertoObservador = 0;
		_ipServidorRemoto = null;
		_puertoServidorRemoto = 0;
	}

    public boolean hayConexionConServidor() { return _ipServidorRemoto == null ? false : true; }

	/**
	 * Conecta al cliente con el servidor.
	 * 
	 * @param ipServidor IP del servidor al que se quiere conectar.
	 * @param puertoServidor Puerto del servidor al que se quiere conectar.
	 * @return 
	 * 		0 - Conexión exitosa;
	 *	   -1 - Falta configurar el cliente;
	 * 	   -2 - ipServidor es nulo, o puertoServidor es menor a 1024 o mayor a 65535;
	 * 	   -3 - Ya existe una conexión con un servidor.
	 * 	   -4 - Error de red al intentar conectarse con el servidor;
	 * 	   -5 - Cualquier otro error no contemplado en el diseño;
	 * 	   -6 - La sala está llena;
     * 	   -7 - El socket utilizado para conectarse ya está registrado.
	 */
	@Override
	public int conectarseAServidor(String ipServidorRemoto, int puertoServidorRemoto) {
		// Verifica que el cliente esté configurado.
		if(_ipHost == null) return -1;

		// Valida el IP y puerto del servidor.
		if(ipServidorRemoto == null) return -2;
		if(puertoServidorRemoto < 1024 || puertoServidorRemoto > 65535) return -2;
		
		// Verifica que no esté actualmente conectado a un servidor.
		if(_ipServidorRemoto != null) return -3;

		try {
			// Se crea el objeto remoto local que servirá para que el servidor remoto pueda
			// notificar sobre los cambios en el modelo a este host, que tendrá el rol de
			// observador.
			IObservador observador = (IObservador) UnicastRemoteObject.exportObject(
				(IObservador) _controladorCliente,
				_puertoObservador
			);
			Registry registroRMI = LocateRegistry.createRegistry(_puertoObservador);
			registroRMI.bind("Observador", observador);
			System.out.println("Observador creado, escuchando en <" + _ipHost + ":" + _puertoObservador + ">.");
		} catch (Exception e) {
			Terminador
				.getInstance()
				.terminarPorExcepcion(e, e.getMessage());
		}

        try {
			// Se obtiene el stub del controlador remoto.
            Registry registroRMI = LocateRegistry.getRegistry(ipServidorRemoto, puertoServidorRemoto);
            IControladorServidor _controladorServidorStub = (IControladorServidor) registroRMI.lookup("ControladorServidor");

			// Se intenta conectar.
			_ipServidorRemoto = ipServidorRemoto;
			_puertoServidorRemoto = puertoServidorRemoto;
			int codigoError = _controladorCliente.conectarseAServidor(_controladorServidorStub, "<" + _ipHost + ":" + _puertoObservador + ">");

			// Verifica resultado de la conexión y devuelve el correspondiente código de error.
			if (codigoError == 0) return codigoError;
			else {
				_ipServidorRemoto = null;
				_puertoServidorRemoto = 0;
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
	 * Configura los parámetros del host, si no está conectado a un servidor.
	 * 
	 * @param ipHost IP que el host utilizará para conectarse como cliente, o para recibir conexiones como observador.
	 * @param puertoObservador Puerto de escucha del host, para recibir notificaciones como observador.
	 * @return
	 * 		0 - Configuración exitosa;
	 * 	   -1 - ipHost es nulo;
	 * 	   -2 - puertoObservador es menor a 1024 o mayor a 65535;
	 * 	   -3 - Hay una conexión en curso y no pueden cambiarse los parámetros de conexión.
	 */
	@Override
	public int configurarCliente(String ipHost, int puertoObservador) {
		if(_ipServidorRemoto != null) { return -3; }
		if(ipHost == null) { return -1; }
		if(puertoObservador < 1024 || puertoObservador > 65535) { return -2; }
		
		_ipHost = ipHost;
		_puertoObservador = puertoObservador;
		return 0;
	}

	/**
	 * Verifica si hay conexion con el servidor.
	 * 
	 * @return
	 * 		0 - Hay conexión con el servidor;
	 * 	   -1 - No hay conexión con el servidor;
	 * 	   -2 - Hubo un error al intentar comunicarse con el servidor;
	 * 	   -3 - Ocurrió un error no previsto.
	 */
	@Override
	public int chequearConexionConServidor() {
		try {
			int codigoError = _controladorCliente.verificarConexionConServidor();

			switch(codigoError) {
				case -1: return -1;
				case -2: return -2;
			}

			return 0;
		} catch (Exception e) { return -3; }
	}

	public String getSocket() { return "<" + _ipHost + ":" + _puertoObservador + ">"; }


	/* Miembros privados. */

	private String _ipHost;
	private int _puertoObservador;
	private String _ipServidorRemoto;
	private int _puertoServidorRemoto;
	private ControladorCliente _controladorCliente;
}