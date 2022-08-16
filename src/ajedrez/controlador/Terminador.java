package ajedrez.controlador;

import ajedrez.modelo.EnumError;

public class Terminador {


    /* Miembros públicos. */

    public static Terminador getInstance() {
        return _instancia;
    }

    /**
     * Termina el programa indicando que no hubo problemas.
     */
    public void terminarOK() {
        System.exit(0);
    }

    /**
     * Termina el programa indicando que hubo un error, imprimiendo
     * en consola una descripción del mismo.
     * 
     * @param codigoError El error ocurrido.
     * @param descripcionError La descripción del error.
     */
    public void terminarPorError(EnumError codigoError, String descripcionError) {
        System.out.println(codigoError.toString()); // Habilitar esta línea para depurar.
        System.exit(1);
    }

    /**
     * Termina el programa indicando que hubo una excepción,
     * imprimiendo en consola una descripción de la misma.
     * 
     * @param excepcion La excepción ocurrida.
     * @param descripcionExcepcion La descripción de la excepción.
     */
    public void terminarPorExcepcion(Exception excepcion, String descripcionExcepcion) {
        excepcion.printStackTrace(); // Habilitar esta línea para depurar.
        System.exit(2);
    }


    /* Miembros privados. */

    static private Terminador _instancia = new Terminador();

}