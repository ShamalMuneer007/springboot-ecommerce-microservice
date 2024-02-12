package org.shamal.productservice.exceptions;

public class ProductValidationError extends RuntimeException {
    public ProductValidationError(String message) {
        super(message);
    }
}
