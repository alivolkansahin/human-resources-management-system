package org.musketeers.exception;

import lombok.Getter;

@Getter
public class CommentServiceException extends RuntimeException{

    private ErrorType errorType;

    public CommentServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public CommentServiceException(ErrorType errorType, String customMessage){
        super(customMessage);
        this.errorType = errorType;
    }
}
