package org.musketeers.exception;

import lombok.Getter;

@Getter
public class AdminServiceException extends RuntimeException{

    private ErrorType errorType;

    public AdminServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public AdminServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
