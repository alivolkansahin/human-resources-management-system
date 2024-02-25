package org.musketeers.rabbitmq.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetAllPendingCommentsResponseModel {

    private String commentId;

    private String companyName;

    private String companyLogo;

    private String personnelName;

    private String personnelLastName;

    private String personnelGender;

    private String personnelImage;

    private String header;

    private String content;

    private Double rating;

    private String creationDate;

}
