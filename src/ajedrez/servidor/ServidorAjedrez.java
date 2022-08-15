package ajedrez.servidor;

import java.rmi.RemoteException;
import ajedrez.controlador.Terminador;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.servidor.Servidor;

public class ServidorAjedrez {


	/* Miembros públicos. */

	public static void main(String[] args) throws InterruptedException {

		// Verifica que los argumentos se hayan pasado correctamente.
		if( args.length == 1 || args.length == 3 || args.length > 4 )
			imprimirInstrucciones();			
		else if( args.length == 2 ) // Se especificó una sola opción.
			if(
				args[0].equals("-ip")
				&& args[1].matches("(\\d{1,3}\\.){3}\\d{1,3}")
			)
				ServidorAjedrez
					.getInstance()
					.ip = args[1];
			else if(
				args[0].equals("-p")
				&& Integer.parseInt(args[1]) >= 1024
				&& Integer.parseInt(args[1]) <= 65535
			)
				ServidorAjedrez
					.getInstance()
					.puerto = Integer.parseInt(args[1]);
			else
				imprimirInstrucciones();			
		else if( args.length == 4 ) {// Se especificaron las dos opciones.
			if(
				args[0].equals("-ip")
				&& args[1].matches("(\\d{1,3}\\.){3}\\d{1,3}")
			)
				ServidorAjedrez
					.getInstance()
					.ip = args[1];
			else if(
				args[0].equals("-p")
				&& Integer.parseInt(args[1]) >= 1024
				&& Integer.parseInt(args[1]) <= 65535
			)
				ServidorAjedrez
					.getInstance()
					.puerto = Integer.parseInt(args[1]);
			else
				imprimirInstrucciones();

			if(
				args[2].equals("-ip")
				&& args[3].matches("(\\d{1,3}\\.){3}\\d{1,3}")
			)
				ServidorAjedrez
					.getInstance()
					.ip = args[3];
			else if(
				args[2].equals("-p")
				&& Integer.parseInt(args[3]) >= 1024
				&& Integer.parseInt(args[3]) <= 65535
			)
				ServidorAjedrez
					.getInstance()
					.puerto = Integer.parseInt(args[3]);
			else
				imprimirInstrucciones();
		}
		 
		// Inicializa el servidor.
		ServidorAjedrez
			.getInstance()
			.servidor = new Servidor(
				ServidorAjedrez
					.getInstance()
					.ip,
				ServidorAjedrez
					.getInstance()
					.puerto
			);
		ServidorAjedrez
			.getInstance()
			.controladorServidor = new ControladorServidor();
		try {
			ServidorAjedrez
				.getInstance()
				.servidor
				.iniciar(
					ServidorAjedrez
						.getInstance()
						.controladorServidor
				);
		} catch (RemoteException e) {
			Terminador
				.getInstance()
				.terminarPorExcepcion(e, e.getMessage());
		} catch (RMIMVCException e) {
			Terminador
				.getInstance()
				.terminarPorExcepcion(e, e.getMessage());
		}
	}

	public static ServidorAjedrez getInstance() {
		return instancia;
	}


	/* Miembros privados. */

	private static ServidorAjedrez instancia = new ServidorAjedrez();
	private Servidor servidor;
	private int puerto; 
	private String ip;
	private ControladorServidor controladorServidor;

	private ServidorAjedrez() {
		ip = "127.0.0.1";
		puerto = 6666;
	}

	/**
	 * Imprime las instrucciones de uso de la aplicación.
	 */
	private static void imprimirInstrucciones() {
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