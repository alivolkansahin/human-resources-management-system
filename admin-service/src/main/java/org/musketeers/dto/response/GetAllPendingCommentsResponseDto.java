package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetAllPendingCommentsResponseDto {

    private String commentId;

    private String companyName;

    private String companyLogo;

    private String personnelName;

    private String personnelLastName;

    private String personnelGender;

    private String personnelImage;

    private String personnelDateOfEmployment;

    private String header;

    private String content;

    private Double rating;

    private String creationDate;

}
