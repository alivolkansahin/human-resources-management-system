package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ErrorType {

    REGISTER_PASSWORD_MISMATCH(1001,"Girilen parolalar uyuşmadı.",HttpStatus.BAD_REQUEST),
    REGISTER_EMAIL_ALREADY_EXISTS(1002,"Bu email adresi daha önce kaydedilmiş.",HttpStatus.BAD_REQUEST),

    DOLOGIN_EMAILORPASSWORD_NOT_EXISTS(2001, "Kullanıcı adı veya şifre yanlış.",HttpStatus.BAD_REQUEST),

    INVALID_TOKEN_FORMAT(3001,"Geçersiz token formatı",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(3002,"Geçersiz token",HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR(5002,"Server hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    PARAMETER_NOT_VALID(3003,"Parametre doğru değil" ,HttpStatus.BAD_REQUEST );

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
