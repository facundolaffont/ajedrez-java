package ajedrez.compartido;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IControladorServidor extends Remote {

    EnumError verificarConexion() throws RemoteException;

    EnumError registrarJugador(String socket, String nombre) throws RemoteException;

    EnumError registrarObservador(String socket) throws RemoteException;

}
