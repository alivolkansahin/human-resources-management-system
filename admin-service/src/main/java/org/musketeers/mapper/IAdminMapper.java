package org.musketeers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.musketeers.dto.request.AdminHandleCommentDecisionRequestDto;
import org.musketeers.dto.response.GetAllPendingCommentsResponseDto;
import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.rabbitmq.model.CommentDecisionModel;
import org.musketeers.rabbitmq.model.GetAllPendingCommentsResponseModel;
import org.musketeers.rabbitmq.model.GetSupervisorResponseModel;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAdminMapper {
    IAdminMapper INSTANCE = Mappers.getMapper(IAdminMapper.class);

    RegisteredSupervisorsResponseDTO supervisorModelToDto(GetSupervisorResponseModel model);

    GetAllPendingCommentsResponseDto getAllPendingCommentsModelToDto(GetAllPendingCommentsResponseModel model);

    @Mapping(target = "decision", expression = "java(dto.getDecision().equalsIgnoreCase(\"true\"))")
    CommentDecisionModel commentDecisionDtoToModel(AdminHandleCommentDecisionRequestDto dto);

}
