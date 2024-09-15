package com.am.crm.customer.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, String id) {
        super(entity + " with ID " + id + " not found.");
    }
}