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
public class SendDayOffStatusChangeMailModel {

    private String name;

    private String lastName;

    private String email;

    private String requestDescription;

    private LocalDate requestStartDate;

    private LocalDate requestEndDate;

    private String updatedStatus;

    private Long requestCreatedAt;

    private Long requestUpdatedAt;

}
