package ajedrez.cliente;

public class AppCliente {
    public static void main(String[] args) {
        /*
		Icon _iconoApp = null;
		URL _imgURL = getClass().getResource("iconos/iconoApp50px.png");
		if (_imgURL != null) {
			_iconoApp = new ImageIcon(_imgURL, "Icono de aplicación");
			this.setIconImage(((ImageIcon) _iconoApp).getImage());
		}
        */

        new ControladorCliente();
    }
}