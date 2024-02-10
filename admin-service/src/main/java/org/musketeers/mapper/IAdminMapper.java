package org.musketeers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.rabbitmq.model.GetSupervisorResponseModel;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAdminMapper {
    IAdminMapper INSTANCE = Mappers.getMapper(IAdminMapper.class);

    RegisteredSupervisorsResponseDTO supervisorModelToDto(GetSupervisorResponseModel model);
}
