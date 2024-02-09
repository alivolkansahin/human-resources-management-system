package org.musketeers.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CompanyServiceException extends RuntimeException{
    private List<ErrorType> errorTypes=new ArrayList<>();

    public CompanyServiceException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorTypes = new ArrayList<>();
        this.errorTypes.add(errorType);
    }

    public CompanyServiceException(ErrorType errorType, String customMesaj) {
        super(customMesaj);
        this.errorTypes = new ArrayList<>();
        this.errorTypes.add(errorType);
    }

    public CompanyServiceException(List<ErrorType> errorTypes) {
        super("Birden Fazla Hata Meydana Geldi.");
        this.errorTypes = errorTypes;
    }

    public CompanyServiceException() {
    }
}
