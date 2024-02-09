package org.musketeers.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.musketeers.entity.enums.PhoneType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Phone {

    private PhoneType phoneType;

    private String phoneNumber;
    @Override
    public String toString() {
        return phoneType.toString() + " "+ phoneNumber;
    }

}
