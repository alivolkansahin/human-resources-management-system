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

    @Builder.Default
    private PhoneType phoneType = PhoneType.PERSONAL;

    private String phoneNumber;

}
