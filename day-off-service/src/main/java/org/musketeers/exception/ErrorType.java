package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    DAY_OFF_NOT_FOUND(5001, "Day off not found with this id!", HttpStatus.NOT_FOUND),   // 404
    EMAIL_ALREADY_EXISTS(5002, "Email already exists!", HttpStatus.CONFLICT),               // 409
    PASSWORD_MISMATCH(5003, "Password mismatch!", HttpStatus.BAD_REQUEST),                  // 400
    INVALID_TOKEN(5004, "Invalid token!", HttpStatus.UNAUTHORIZED),                         // 401
    INVALID_PERSON(5005, "You can only cancel your own request!", HttpStatus.CONFLICT),
    INVALID_PARAMETER(5002,"Invalid parameter" ,HttpStatus.BAD_REQUEST ),
    INVALID_ROLE(5006, "Invalid role", HttpStatus.FORBIDDEN),
    SERVICE_NOT_RESPONDING(5010, "Your request cannot be processed at the moment", HttpStatus.SERVICE_UNAVAILABLE),
    PENDING_REQUEST_EXISTS(5007,"A pending request already exists for the personnel, so another request cannot be created!", HttpStatus.CONFLICT);// 401

    private int code;
    private String message;
    private HttpStatus httpStatus;

}
