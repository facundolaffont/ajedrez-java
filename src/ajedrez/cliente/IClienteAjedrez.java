package ajedrez.cliente;

import ajedrez.modelo.EnumError;

public interface IClienteAjedrez {

    EnumError configurarCliente(String ip, int puerto);

    EnumError conectarseAServidor(String ip, int puerto);

    EnumError enviarPingAlServidor();

}
