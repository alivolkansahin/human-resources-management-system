package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SendAdvanceStatusChangeNotificationModel {

    private String personnelId;

    private String requestReason;

    private String requestDescription;

    private Double requestAmount;

    private String updatedStatus;

    private Long requestCreatedAt;

    private Long requestUpdatedAt;

}
