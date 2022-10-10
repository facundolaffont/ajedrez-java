package ajedrez.cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Permite que el servidor interactúe con esta
 * interfaz utilizando RMI.
 */
interface IObservador extends Remote {

    public void actualizar(Object mensaje) throws RemoteException;

}
