package org.musketeers.exception;

import lombok.Getter;

@Getter
public class AdvanceServiceException extends RuntimeException{

    private ErrorType errorType;

    public AdvanceServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public AdvanceServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
