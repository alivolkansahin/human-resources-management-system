package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    GUEST_NOT_FOUND(5001, "Guest not found with this id!", HttpStatus.NOT_FOUND),   // 404
    EMAIL_ALREADY_EXISTS(5002, "Email already exists!", HttpStatus.CONFLICT),               // 409
    PASSWORD_MISMATCH(5003, "Password mismatch!", HttpStatus.BAD_REQUEST),                  // 400
    INVALID_TOKEN(5004, "Invalid token!", HttpStatus.UNAUTHORIZED);                         // 401

    private int code;
    private String message;
    private HttpStatus httpStatus;

}
