package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdatePersonnelRequestDto {

    private String token;

    private String name;

    private String lastName;

    private String email;

    private List<String> phones;

    private String profileImageUrl;

}
