package org.musketeers.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorMessage {

    private ErrorType code;
    private String message;
    private List<String> fields;

    public void addField(String field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }
}
