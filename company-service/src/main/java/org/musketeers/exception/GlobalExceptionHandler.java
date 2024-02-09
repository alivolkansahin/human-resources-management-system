package org.musketeers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CompanyServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleManagerException(CompanyServiceException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<ErrorType> errorTypes = exception.getErrorTypes();
        List<String> errorMessages = new ArrayList<>();
        for (ErrorType errorType : errorTypes) {
            httpStatus = errorType.getHttpStatus();
            errorMessages.add(errorType.getMessage());
        }
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("Hatalar:");
        errorMessage.setFields(errorMessages);

        return new ResponseEntity<>(errorMessage, httpStatus);
    }

    private ErrorMessage createError(ErrorType errorType, Exception e){
        return ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }
}
