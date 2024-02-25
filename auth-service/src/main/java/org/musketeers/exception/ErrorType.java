package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public enum ErrorType {

    REGISTER_PASSWORD_MISMATCH(1001,"The entered passwords did not match !",HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1002,"The email address you entered is being used by someone else !",HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1003,"The phone you entered is being used by someone else !",HttpStatus.BAD_REQUEST),
    NOT_FOUND(1004,"User not found !",HttpStatus.NOT_FOUND),
    INVALID_PASSWORD_OR_EMAIL(1005,"The entered password or email is incorrect !",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(1006,"Your account is not active !",HttpStatus.BAD_REQUEST),
    ACCOUNT_DELETED(1007,"Account has been deleted before !",HttpStatus.NOT_FOUND),
    TOKEN_NOT_CREATED(1008,"Token could not be created !",HttpStatus.BAD_REQUEST),
    COMPANY_CONTRACT_EXPIRED(1009, "Company contract has expired!", HttpStatus.BAD_REQUEST),

    DOLOGIN_EMAILORPASSWORD_NOT_EXISTS(2001, "Kullanıcı adı veya şifre yanlış.",HttpStatus.BAD_REQUEST),

    INVALID_TOKEN_FORMAT(3001,"Geçersiz token formatı",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(3002,"Geçersiz token",HttpStatus.BAD_REQUEST),

    PARAMETER_NOT_VALID(5002,"Parameter is incorrect" ,HttpStatus.BAD_REQUEST ),

    SERVICE_NOT_RESPONDING(5010, "Your request cannot be processed at the moment", HttpStatus.SERVICE_UNAVAILABLE),
    COMPANY_NOT_FOUND(1010, "Company not found!", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_EXIST(1011,"Company name already exists!", HttpStatus.BAD_REQUEST);



    private int code;
    private String message;
    private HttpStatus httpStatus;
}
