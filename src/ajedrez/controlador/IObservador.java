package ajedrez.controlador;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObservador extends Remote {
   public void actualizar(Object mensaje) throws RemoteException;
}
