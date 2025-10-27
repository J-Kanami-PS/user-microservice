package org.example.cuidadodemascotas.usermicroservice.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Long id, Class<?> entityClass) {
        super(String.format("%s no encontrado con id: %d",
                entityClass.getSimpleName(), id));
    }
}