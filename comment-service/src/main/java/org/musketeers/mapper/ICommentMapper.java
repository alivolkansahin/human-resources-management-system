package org.musketeers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.musketeers.dto.response.PersonnelDetailsResponseDto;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsByCommentResponseModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICommentMapper {

    ICommentMapper INSTANCE = Mappers.getMapper(ICommentMapper.class);

    PersonnelDetailsResponseDto personnelDetailsModelToDto(GetPersonnelDetailsByCommentResponseModel model);
    
}
