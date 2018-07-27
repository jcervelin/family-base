package io.jcervelin.familybase.domains.exceptions;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
public class UnprocessableEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnprocessableEntityException(final String message) {
        super(message);
    }
}
