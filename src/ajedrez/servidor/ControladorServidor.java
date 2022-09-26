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

    @Override
    public EnumError registrarJugador(String nombre) throws RemoteException {
        /* Armar una estructura que mapee el ID del objeto que llama a un jugador.
         * Crearía un jugador nuevo y lo guardaría en ese lugar, sin importar
         * si hay un objeto ahí o no, ya, porque dicha referencia, al perderse,
         * se limpiaría gracias al colector de basura.
         * 
         * También debería crear un objeto Juego, y el controlador debería
         * conocerlo y crearlo ni bien se crea el controlador.
         */
        return null;
    }

}