package org.musketeers.exception;

import lombok.Getter;

@Getter
public class PersonnelServiceException extends RuntimeException{

    private ErrorType errorType;

    public PersonnelServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public PersonnelServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
