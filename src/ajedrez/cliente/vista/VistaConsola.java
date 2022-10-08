package ajedrez.cliente.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import ajedrez.cliente.IClienteAjedrez;
import ajedrez.cliente.IControladorCliente;
import ajedrez.cliente.IJugador;
import ajedrez.compartido.EnumError;
import ajedrez.compartido.EnumEstadoDeJuego;
import ajedrez.compartido.Terminador;

public class VistaConsola extends JFrame implements IVista {
	

	/* Miembros públicos. */
	
	public VistaConsola(IControladorCliente iControlador, IJugador iJugador, IClienteAjedrez iClienteAjedrez) {	

		// Creación y configuración de la ventana.
		super();
		_estadoDelJuego = EnumEstadoDeJuego.SIN_PARTIDA;
		_iJugador = iJugador;
		_iClienteAjedrez = iClienteAjedrez;
		_iControlador = iControlador;

		// Configuraciones de la ventana.
		_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		_ancho = 800;
		_alto = 400;
		this.setSize(_ancho, _alto);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocation((_pantalla.width / 2) - (_ancho / 2), (_pantalla.height / 2) - (_alto / 2));
		this.setResizable(false);
		
		// Cargar y configurar el ícono de la ventana.		
		_iconoApp = null;
		_imgURL = this.getClass().getResource("iconos/iconoApp50px.png"); // TODO: #2 La imagen no se está cargando.
		if (_imgURL != null) {
			_iconoApp = new ImageIcon(_imgURL, "Icono de aplicación");
			this.setIconImage(((ImageIcon) _iconoApp).getImage());
		}
		
		// Creación y configuración de los paneles.
		_panelPrincipal = (JPanel) this.getContentPane();
		_panelPrincipal.setLayout(new BorderLayout());
		_panelPrincipal.setVisible(true);
		
		// Creación y configuración de la consola.
		_consola = new JTextArea();
		_consola.setBounds(0, 0, 500, 350);
		_consola.setEditable(false);
		_consola.setMargin(new Insets(10, 10, 10, 10));
		_consola.setLineWrap(true); // TODO: #3 Se colapsa la consola cuando setLineWrap es false.
		_consola.setTabSize(1);
		_consola.setBackground(Color.BLACK);
		_consola.setForeground(Color.WHITE);
		_consola.setAutoscrolls(true);
		_consola.setFont(new Font("monospaced", Font.PLAIN, 12));
		_caret = (DefaultCaret) _consola.getCaret();
		_caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		_consolaTablero = new JTextArea();
		_consolaTablero.setBounds(0, 0, 300, 350);
		_consolaTablero.setEditable(false);
		_consolaTablero.setMargin(new Insets(10, 10, 10, 10));
		_consolaTablero.setLineWrap(true);
		_consolaTablero.setTabSize(1);
		_consolaTablero.setBackground(Color.BLACK);
		_consolaTablero.setForeground(Color.WHITE);
		_consolaTablero.setAutoscrolls(true);
		_consolaTablero.setFont(new Font("monospaced", Font.PLAIN, 12));
		_panelConsola = new JScrollPane(_consola, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_panelConsolaTablero = new JScrollPane(_consolaTablero, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		_panelPrincipal.add(_panelConsola, BorderLayout.WEST);
		_panelPrincipal.add(_panelConsolaTablero, BorderLayout.EAST);
		
		// Creación y configuración del campo de ingreso de texto.
		_textoComandos = new JTextField();
		_panelPrincipal.add(_textoComandos, BorderLayout.SOUTH);		
		
		crearActionListener();
	}
	
	public void mostrarVentana() {
		this.setVisible(true);
		_textoComandos.requestFocus();
	}
	

	/* Miembros privados. */
	
	private IControladorCliente _iControlador;
	private IClienteAjedrez _iClienteAjedrez;
	private IJugador _iJugador;
	private Dimension _pantalla;
	private int _ancho,
				_alto;
	private URL _imgURL;
	private Icon _iconoApp;
	private EnumEstadoDeJuego _estadoDelJuego;
	private DefaultCaret _caret;
	private JPanel _panelPrincipal;
	private JScrollPane _panelConsola,
						_panelConsolaTablero;
	private JTextArea 	_consola,
						_consolaTablero;
	private JTextField _textoComandos;
	
	private void crearActionListener() {

	_textoComandos.addActionListener(new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = ( (JTextField) e.getSource() ).getText();
		
		if( _textoComandos.getText().isEmpty() ) return;


		/* Comandos habilitados sólo para estados de partida específicos. */

		switch(_estadoDelJuego) {

		case SIN_PARTIDA:
		// Comandos habilitados cuando no hay partida.
			
			// salir
			if( comando.equals("salir") ) {
				_consola.append("\n\n> " + comando);
				Terminador.getInstance().terminarOK();
			}

			// configurarcliente, conf
			else if(comando.matches(
				"("
					+ "^(configurarcliente|conf)"
					+ "(" + "\\s" + "(" + "\\d{1,3}" + "\\." + ")" + "{3}" + "\\d{1,3}" + ")" + "{0,1}" // IP del cliente.
					+ "\\s" + "\\d{4,5}" // Puerto del cliente.
					+ "$"
				+ ")"
			)) {

				_consola.append("\n\n> " + comando);

				// Valida la IP (si se ingresó) y el puerto.
				String[] tokens = comando.split("\\s");
				int puerto =
					tokens.length == 3
					? Integer.parseInt(tokens[2])
					: Integer.parseInt(tokens[1]);
				String ip =
					tokens.length == 3
					? tokens[1]
					: "127.0.0.1";
				if(!esPuertoValido(puerto)) {
					_consola.append("\nEl puerto debe ser un entero entre 1024 y 65535."
						+ "\n"
					);
					return;
				} else if(!esIpValida(ip)) {
					_consola.append("\nEl IP debe estar formado por enteros entre 0 y 255."
						+ "\n"
					);
					return;
				}

				// Configura el cliente.
				else {
					EnumError codigoError = _iClienteAjedrez.configurarCliente( ip, puerto );
					if( codigoError != EnumError.SIN_ERROR )
						_consola.append("\n\nError al configurar el cliente: " + codigoError.toString() + "."
							+ "\n"
						);
					else
						_consola.append("\n\nCliente configurado exitosamente."
							+ "\n"
						);
				}

			}

			// conectaraservidor, cs
			else if(comando.matches(
				"^(conectaraservidor|cs)"
				+ "(" + "\\s" + "(" + "\\d{1,3}" + "\\." + ")" + "{3}" + "\\d{1,3}" + ")" + "{0,1}" // IP del servidor.
				+ "\\s" + "\\d{4,5}" // Puerto del servidor.
				+ "$"
			)) {

				_consola.append("\n\n> " + comando);

				// Valida la IP y el puerto.
				String[] tokens = comando.split("\\s");
				int puerto =
					tokens.length == 3
					? Integer.parseInt(tokens[2])
					: Integer.parseInt(tokens[1]);
				String ip =
					tokens.length == 3
					? tokens[1]
					: "127.0.0.1";
				if(!esPuertoValido(puerto)) {
					_consola.append("\n\nEl puerto debe ser un entero entre 1024 y 65535."
						+ "\n"
					);
					return;
				} else if(!esIpValida(ip)) {
					_consola.append("\n\nEl IP debe estar formado por enteros entre 0 y 255."
						+ "\n"
					);
					return;
				}

				// Intenta conectarse al servidor.
				else {
					EnumError codigoError = _iClienteAjedrez.conectarseAServidor( ip, puerto ); // TODO: #4 No permite conectar después de intento fallido.
					if( codigoError != EnumError.SIN_ERROR)
						_consola.append("\n\nError al conectarse con el servidor: " + codigoError.toString() + "."
							+ "\n"
						);
					else
						_consola.append("\n\nConexión exitosa con el servidor."
							+ "\n"
						);
				}

			}
			
			// Otro comando, no necesariamente incorrecto.
			else break;

			// Si se llegó hasta acá, el comando ingresado es correcto,
			// y pertenece al grupo de comandos que se deben interpretar
			// cuando el estado de la partida es SIN_PARTIDA.
			_textoComandos.setText("");
			return;
			
		// Con cualquier otro estado, no hay exclusividad de comandos.
		default: break;

		} // switch


		/* Comandos habilitados en todos los estados de partida. */

		// chequearconexion, ch
		if( comando.matches("^(chequearconexion|ch)$") ) {
			_consola.append("\n\n> " + comando
				+ "\n\nMensaje de verificación enviado al servidor..."
				+ "\n"
			);
			EnumError codigoError = _iClienteAjedrez.chequearConexionConServidor();
			if( codigoError == EnumError.SIN_ERROR )
				_consola.append("\n\nSe recibió respuesta del servidor!"
					+ "\n"
				);
			else
				_consola.append("\n\nOcurrió un problema: "
					+ codigoError.toString() + ".\n"
				);
			_textoComandos.setText("");
			return;
		}
		
		// ayuda, ?
		else if( comando.matches("^(ayuda|\\?)$") ) {
			_consola.append("\n\n> " + comando);
			mostrarComandos();
			_textoComandos.setText("");
			return;
		}
			
		// clear, cls
		else if( comando.matches("^(clear|cls)$") ) {
			_consola.setText("");
			_textoComandos.setText("");
			return;
		}
			
		// Comando incorrecto.
		_consola.append("\n\n> " + comando
			+ "\n\nEl comando que ingresó es incorrecto."
			+ "\n\nIngrese \"ayuda\" ó \"?\" para información sobre los posibles comandos."
			+ "\n"
		);

	} // public void actionPerformed(ActionEvent e) ...
	}); // _textoComandos.addActionListener(...
	} // private void crearActionListener() ...
	
	private void mostrarComandos() {

		_consola.append("\n\nCOMANDOS DISPONIBLES");

		switch(_estadoDelJuego) {

		case SIN_PARTIDA:
		// Comandos habilitados cuando no hay partida.
			
			// configurarcliente, conf
			_consola.append("\n\n    (\"configurarcliente\"|\"conf\") [ip] puerto"
				+ "\n        Configura la IP y puerto que tendrá el cliente para conectarse al servidor."
				+ "\n        Si se omite el IP, se toma localhost como IP predeterminada."
			);

			// conectaraservidor, cs
			_consola.append("\n\n    (\"conectaraservidor\"|\"cs\") [ip] puerto"
				+ "\n        Se conecta al servidor de ajedrez con IP y puerto especificados."
				+ "\n        Si se omite el IP, se toma localhost como IP predeterminada."
			);

			// salir
			_consola.append("\n\n    \"salir\""
				+ "\n        para salir de la aplicación."
			);

		break;

		default: break;

		} // switch


		/* Comandos habilitados en todos los estados de partida. */

		// chequearconexion, ch
		_consola.append("\n\n    (\"chequearconexion\"|\"ch\")"
			+ "\n        Envía al servidor un mensaje de verificación de estado de la conexión."
		);

		// conectaraservidor, cs
		_consola.append("\n\n    (\"conectaraservidor\"|\"cs\") [ip] puerto"
			+ "\n        Se conecta a un servidor de ajedrez con el <ip> y <puerto> indicados."
			+ "\n        Si se omite la <ip>, por defecto se utiliza la ip localhost."
		);

		// ayuda, ?
		_consola.append("\n\n    (\"ayuda\"|\"?\")"
			+ "\n        Para mostrar este mensaje."
		);

		// clear, cls
		_consola.append("\n\n    (\"clear\"|\"cls\")"
			+ "\n        Para limpiar la consola."
		);

	}
	
	/**
	 * Verifica que el puerto sea un entero entre 1024 y 65535.
	 * 
	 * @param puerto El puerto a verificar.
	 * @return true, si el puerto es un entero entre 1024 y 65535;
	 * false, de lo contrario.
	 */
	private boolean esPuertoValido(int puerto) {
		if( puerto < 1024 || puerto > 65535 ) return false;
		else return true;
	}

	/**
	 * Verifica que los enteros de una IPv4 no sean mayores que 255.
	 * 
	 * @param ip Una dirección IPv4.
	 * @return true, si los enteros de la ip no son mayores que 255; false, de lo contrario.
	 */
	private boolean esIpValida(String ip) {
		String[] componentesDeIp = ip.split("\\.");
		for (String componente : componentesDeIp)
			if( Integer.parseInt(componente) > 255 ) return false;

		return true;
	}

}