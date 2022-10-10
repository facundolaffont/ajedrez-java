package ajedrez.cliente;

import ajedrez.compartido.EnumError;

public interface IClienteAjedrez {

    EnumError configurarCliente(String ip, int puerto);

    EnumError conectarseAServidor(String ip, int puerto);

    EnumError chequearConexionConServidor();

}
