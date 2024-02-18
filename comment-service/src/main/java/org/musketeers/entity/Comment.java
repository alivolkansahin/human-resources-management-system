package org.musketeers.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.entity.enums.EActivationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Document(collection = "comments")
public class Comment extends BaseEntity {

    @Id
    private String id;

    private String companyId;

    private String personnelId;

    private String header;

    private String content;

    @Builder.Default
    private Double rating = 0d;

    private List<String> ratersAuthIds;

    @Builder.Default
    private EActivationStatus activationStatus = EActivationStatus.PENDING;

}
