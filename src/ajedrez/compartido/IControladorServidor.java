package ajedrez.compartido;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IControladorServidor extends Remote {

    int verificarConexion() throws RemoteException;

    int registrarJugador(String socket, String nombre) throws RemoteException;

    int registrarObservador(String socket) throws RemoteException;

    int iniciarPartida() throws RemoteException;

}
