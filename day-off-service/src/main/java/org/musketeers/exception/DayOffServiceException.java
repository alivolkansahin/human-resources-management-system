package org.musketeers.exception;

import lombok.Getter;

@Getter
public class DayOffServiceException extends RuntimeException{

    private ErrorType errorType;

    public DayOffServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public DayOffServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
