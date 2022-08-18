package ajedrez.servidor;

import java.rmi.RemoteException;
import ajedrez.modelo.EnumError;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class ControladorServidor extends ObservableRemoto implements IControladorServidor {


    /* Miembros públicos. */

    /**
     * Sólo devuelve un valor para verificar que la conexión está en
     * buen estado.
     * 
     * @return SIN_ERROR - La conexión está en buen estado.
     */
    @Override
    public EnumError chequearConexion() throws RemoteException {
        return EnumError.SIN_ERROR;
    }

}