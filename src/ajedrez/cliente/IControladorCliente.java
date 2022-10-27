package ajedrez.cliente;

import ajedrez.compartido.EnumError;

/**
 * Con esta interfaz interactuará la vista para
 * solicitar servicios directamente al controlador
 * del cliente.
 */
public interface IControladorCliente {
    public EnumError iniciarPartida();
}
