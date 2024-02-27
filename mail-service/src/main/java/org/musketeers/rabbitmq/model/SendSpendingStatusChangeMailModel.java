package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SendSpendingStatusChangeMailModel {

    private String name;

    private String lastName;

    private String email;

    private String requestDescription;

    private Double requestAmount;

    private String requestCurrency;

    private LocalDate requestSpendingDate;

    private List<String> requestAttachments;

    private String updatedStatus;

    private Long requestCreatedAt;

    private Long requestUpdatedAt;

}
