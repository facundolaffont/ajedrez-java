package ajedrez.servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import ajedrez.compartido.IControladorServidor;
import ajedrez.compartido.Terminador;

class ServidorAjedrez {

	/* Miembros públicos. */

	public static void main(String[] args) throws InterruptedException {

		// Verifica que los argumentos se hayan pasado correctamente.
		if( args.length == 1 || args.length == 3 || args.length > 4 )
			_imprimirInstrucciones();			
		else if( args.length == 2 ) // Se especificó una sola opción.
			if(
				args[0].equals("-ip")
				&& args[1].matches("(\\d{1,3}\\.){3}\\d{1,3}")
			)
				_setIP(args[1]);
			else if(
				args[0].equals("-p")
				&& Integer.parseInt(args[1]) >= 1024
				&& Integer.parseInt(args[1]) <= 65535
			)
				_setPuerto(Integer.parseInt(args[1]));
			else
				_imprimirInstrucciones();			
		else if( args.length == 4 ) {// Se especificaron las dos opciones.
			if(
				args[0].equals("-ip")
				&& args[1].matches("(\\d{1,3}\\.){3}\\d{1,3}")
			)
				_setIP(args[1]);
			else if(
				args[0].equals("-p")
				&& Integer.parseInt(args[1]) >= 1024
				&& Integer.parseInt(args[1]) <= 65535
			)
				_setPuerto(Integer.parseInt(args[1]));
			else
				_imprimirInstrucciones();

			if(
				args[2].equals("-ip")
				&& args[3].matches("(\\d{1,3}\\.){3}\\d{1,3}")
			)
				_setIP(args[3]);
			else if(
				args[2].equals("-p")
				&& Integer.parseInt(args[3]) >= 1024
				&& Integer.parseInt(args[3]) <= 65535
			)
				_setPuerto(Integer.parseInt(args[3]));
			else
				_imprimirInstrucciones();
		}

		// Crea el controlador del servidor y guarda su stub en el registro RMI.
		try {
			_setControladorServidor(new ControladorServidor());
            _setControladorServidorStub(
				(IControladorServidor)
                UnicastRemoteObject.exportObject(
					_getControladorServidor(),
					_getPuerto()
				)
			);
			Registry registroRMI = LocateRegistry.createRegistry(_getPuerto());
            registroRMI.bind("ControladorServidor", _getControladorServidorStub());
        	System.out.println("Servidor creado en <" + _getIP() + ":" + _getPuerto() + ">.");
        } catch (Exception e) {
			e.printStackTrace();
			/*
            Terminador
				.getInstance()
				.terminarPorExcepcion(e, e.getMessage());
			*/
        } 
	}

	public static ServidorAjedrez getInstance() {
		return _instancia;
	}


	/* Miembros privados. */

	private static ServidorAjedrez _instancia = new ServidorAjedrez();
	private IControladorServidor _controladorServidorStub;
	private int _puerto; 
	private String _ip;
	private ControladorServidor _controladorServidor;

	private ServidorAjedrez() {
		_ip = "127.0.0.1";
		_puerto = 6666;
	}

	private static String _getIP() {
		return ServidorAjedrez.getInstance()._ip;
	}

	private static int _getPuerto() {
		return ServidorAjedrez.getInstance()._puerto;
	}

	private static ControladorServidor _getControladorServidor() {
		return ServidorAjedrez.getInstance()._controladorServidor;
	}

	private static IControladorServidor _getControladorServidorStub() {
		return ServidorAjedrez.getInstance()._controladorServidorStub;
	}

	private static void _setIP(String ip) {
		ServidorAjedrez.getInstance()._ip = ip;
	}

	private static void _setPuerto(int puerto) {
		ServidorAjedrez.getInstance()._puerto = puerto;
	}

	private static void _setControladorServidor(ControladorServidor controladorServidor) {
		ServidorAjedrez.getInstance()._controladorServidor = controladorServidor;
	}

	private static void _setControladorServidorStub(IControladorServidor controladorServidorStub) {
		ServidorAjedrez.getInstance()._controladorServidorStub = controladorServidorStub;
	}

	/**
	 * Imprime las instrucciones de uso de la aplicación.
	 */
	private static void _imprimirInstrucciones() {
		System.out.println(
			"SINOPSIS\n"
				+ "\t\"java\" \"ServidorAjedrez\" [\"-ip\" ip-servidor] [\"-p\" puerto]\n\n"

			+ "DESCRIPCIÓN\n"
				+ "\t\"-ip\" ip-servidor\n"
					+ "\t\tEl IPv4 en el que escuchará el servidor. Por defecto es localhost.\n\n"
				+ "\t\"-p\" puerto\n"
					+ "\t\tEl puerto en el que escuchará el servidor. Por defecto es 6666."
		);

		Terminador
			.getInstance()
			.terminarOK();
	}

}