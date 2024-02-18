package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetAllCommentsByCompanyResponseDto {

    private String commentId;

    private PersonnelDetailsResponseDto personnel;

    private String header;

    private String content;

    private String creationDate;

    private Double rating;

}
