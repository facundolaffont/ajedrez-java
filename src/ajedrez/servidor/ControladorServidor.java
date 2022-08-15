package ajedrez.servidor;

import java.rmi.RemoteException;
import ajedrez.modelo.EnumError;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class ControladorServidor extends ObservableRemoto implements IControladorServidor {


    /* Miembros públicos. */

    @Override
    public EnumError ping() throws RemoteException {
        return EnumError.SIN_ERROR;
    }

}