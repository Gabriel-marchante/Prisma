package org.padalustro.domain.exceptions;

/**
 * Excepció de negoci.
 * 
 * Aquesta classe s'utilitza quan es vol accedir o eliminar
 * un VideoCataleg que NO existeix en la base de dades.
 * 
 * NO és un error tècnic (SQL, NullPointer, etc.)
 * És una regla de negoci
 */
public class VideoCatalegNotFoundException extends RuntimeException {

    /**
     * Constructor que rep l'id del vídeo que no s'ha trobat
     * i construeix un missatge d'error descriptiu.
     */
    public VideoCatalegNotFoundException(Long id) {
        super("VideoCataleg amb id " + id + " no trobat");
    }
}
