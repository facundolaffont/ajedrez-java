package ajedrez.servidor;

import java.rmi.RemoteException;
import ajedrez.modelo.EnumError;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public interface IControladorServidor extends IObservableRemoto {

    EnumError ping() throws RemoteException;

}