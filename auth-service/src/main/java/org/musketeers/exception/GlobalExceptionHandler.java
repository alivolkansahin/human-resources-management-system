package org.musketeers.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException exception){
        return ResponseEntity.ok(ErrorMessage.builder()
                        .code(ErrorType.INTERNAL_SERVER_ERROR)
                        .message("Runtime Error : "+exception.getMessage())
                .build());
    }

    @ExceptionHandler(AuthServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleDemoException(AuthServiceException exception){
        ErrorMessage errorMessage = createError(exception.getErrorType(), exception);
        errorMessage.addField(exception.getMessage());
        return ResponseEntity.
                status(exception.getErrorType().getHttpStatus()).
                body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ErrorType errorType = ErrorType.PARAMETER_NOT_VALID;
        List<String> fields = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e-> fields.add(e.getField()+": " + e.getDefaultMessage()));
        ErrorMessage errorMessage=createError(errorType,ex);
        errorMessage.setFields(fields);
        return  new ResponseEntity<>(errorMessage,errorType.getHttpStatus());
    }

    private ErrorMessage createError(ErrorType errorType, Exception ex){
        System.out.println("Hata olustu: "+ex.getMessage());
        return ErrorMessage.builder()
                .code(errorType)
                .message(errorType.getMesaj())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAllExceptions(Exception exception) {
        ErrorMessage errorMessage = createError(ErrorType.INTERNAL_SERVER_ERROR, exception);
        errorMessage.addField(exception.getMessage());

        return ResponseEntity
                .status(errorMessage.getCode().getHttpStatus())
                .body(errorMessage);
    }
}
