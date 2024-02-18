package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    COMMENT_NOT_FOUND(5001, "Comment not found with this id!", HttpStatus.NOT_FOUND),       // 404
    INVALID_TOKEN(5004, "Invalid token!", HttpStatus.UNAUTHORIZED);                         // 401

    private int code;
    private String message;
    private HttpStatus httpStatus;

}
