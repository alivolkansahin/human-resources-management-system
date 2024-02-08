package org.musketeers.exception;

import lombok.Getter;

@Getter
public class GuestServiceException extends RuntimeException{

    private ErrorType errorType;

    public GuestServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public GuestServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
