package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ErrorType {
    INVALID_TOKEN(5101,"Gecersiz token",HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_TOKEN(5102,"Goruntuleme icin yetkiniz yok",HttpStatus.UNAUTHORIZED),
    COMPANY_NOT_FOUND(5103,"Boyle bir company bulunamadi" ,HttpStatus.NOT_FOUND);

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
