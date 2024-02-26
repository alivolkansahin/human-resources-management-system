package org.musketeers.exception;

import lombok.Getter;

@Getter
public class SpendingServiceException extends RuntimeException{

    private ErrorType errorType;

    public SpendingServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public SpendingServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
