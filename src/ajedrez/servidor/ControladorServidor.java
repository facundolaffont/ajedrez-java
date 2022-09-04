package ajedrez.servidor;

import java.rmi.RemoteException;
import ajedrez.modelo.EnumError;

public class ControladorServidor implements IControladorServidor {


    /* Miembros públicos. */

    /**
     * Sólo devuelve un valor para verificar que la conexión está en
     * buen estado.
     * 
     * @return SIN_ERROR - La conexión está en buen estado.
     */
    @Override
    public EnumError verificarConexion() throws RemoteException {
        return EnumError.SIN_ERROR;
    }

}