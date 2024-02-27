package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateSupervisorModel {

    private String authId;

    private String name;

    private String lastName;

    private String email;

    private List<String> phones;

    private String image;

}
