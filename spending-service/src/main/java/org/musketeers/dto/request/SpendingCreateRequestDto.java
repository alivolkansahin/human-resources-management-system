package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpendingCreateRequestDto {

    private String token;

    private String reason;

    private String description;

    private Double amount;

    private String currency;

    private LocalDate spendingDate;

    private List<MultipartFile> attachments;

}
