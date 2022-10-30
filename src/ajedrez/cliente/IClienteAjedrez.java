package ajedrez.cliente;

public interface IClienteAjedrez {

    int configurarCliente(String ip, int puerto);

    int conectarseAServidor(String ip, int puerto);

    int chequearConexionConServidor();

}
