package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorMessage { // kullanıcıya ne döneceksek onları tanımlayalım

    private int code;

    private String message;
}
