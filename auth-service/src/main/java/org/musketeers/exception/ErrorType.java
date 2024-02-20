package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ErrorType {

    REGISTER_PASSWORD_MISMATCH(1001,"The entered passwords did not match !",HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1002,"The email address you entered is being used by someone else !",HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1003,"The phone you entered is being used by someone else !",HttpStatus.BAD_REQUEST),
    NOT_FOUND(1004,"Auth not found !",HttpStatus.NOT_FOUND),
    INVALID_PASSWORD_OR_EMAIL(1005,"The entered password or email is incorrect !",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(1006,"Your account is not active !",HttpStatus.BAD_REQUEST),
    ACCOUNT_DELETED(1007,"Account has been deleted before !",HttpStatus.NOT_FOUND),
    TOKEN_NOT_CREATED(1008,"Token could not be created !",HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR(5002,"Server Error",HttpStatus.INTERNAL_SERVER_ERROR),
    PARAMETER_NOT_VALID(3003,"Paramater is incorrect !" ,HttpStatus.BAD_REQUEST );

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
