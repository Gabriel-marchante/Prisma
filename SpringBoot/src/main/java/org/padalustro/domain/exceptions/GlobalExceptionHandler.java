package org.padalustro.domain.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe global que captura excepcions llançades
 * des de qualsevol controller de l'aplicació.
 * 
 * Actua com a "traductor" entre:
 * - Errors de negoci (excepcions)
 * - Respostes HTTP (status + body)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Aquest mètode s'executa AUTOMÀTICAMENT quan
     * es llança una VideoCatalegNotFoundException
     * en qualsevol punt del projecte.
     * 
     * @param ex Excepció llançada pel UseCase
     * @return Resposta HTTP 404 amb un missatge d'error
     */
    @ExceptionHandler(VideoCatalegNotFoundException.class)
    public ResponseEntity<String> handleVideoNotFound(VideoCatalegNotFoundException ex) {

        // Retornem un 404 (Not Found)
        // amb el missatge definit en l'excepció
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
