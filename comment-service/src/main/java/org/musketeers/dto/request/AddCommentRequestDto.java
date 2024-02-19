package org.musketeers.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddCommentRequestDto {

    private String token;

    private String header;

    private String content;

    private Double rating;

}
