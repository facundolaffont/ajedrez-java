package ajedrez.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import ajedrez.modelo.EnumError;

public interface IControladorServidor extends Remote {

    EnumError verificarConexion() throws RemoteException;

    EnumError registrarJugador(String nombre) throws RemoteException;

    EnumError registrarObservador(String ipDeCliente, int puertoDeCliente) throws RemoteException;

}