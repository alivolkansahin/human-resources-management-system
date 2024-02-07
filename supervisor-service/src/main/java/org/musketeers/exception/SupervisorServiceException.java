package org.musketeers.exception;

import lombok.Getter;

@Getter
public class SupervisorServiceException extends RuntimeException{

    private ErrorType errorType;

    public SupervisorServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public SupervisorServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
