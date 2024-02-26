package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    SPENDING_NOT_FOUND(5001, "Spending not found with this id!", HttpStatus.NOT_FOUND),   // 404
    INVALID_TOKEN(5004, "Invalid token!", HttpStatus.UNAUTHORIZED),                         // 401
    INVALID_PERSON(5005, "You can only cancel your own request!", HttpStatus.CONFLICT),
    INVALID_SUPERVISOR(5008,"You can only update your own company spending requests!", HttpStatus.CONFLICT),
    INVALID_PARAMETER(5002,"Invalid parameter" ,HttpStatus.BAD_REQUEST),
    INVALID_ROLE(5006, "Invalid role", HttpStatus.FORBIDDEN),
    SERVICE_NOT_RESPONDING(5010, "Your request cannot be processed at the moment", HttpStatus.SERVICE_UNAVAILABLE),
    PENDING_REQUEST_EXISTS(5007,"A pending request already exists for the personnel, so another request cannot be created!", HttpStatus.CONFLICT); // 401

    private int code;
    private String message;
    private HttpStatus httpStatus;

}
