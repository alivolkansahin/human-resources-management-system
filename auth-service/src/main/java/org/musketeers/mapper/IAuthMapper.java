package org.musketeers.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.musketeers.dto.request.GuestRegisterRequestDto;
import org.musketeers.entity.Auth;
import org.musketeers.rabbitmq.model.MailSenderGuestModel;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {

    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);


    Auth guestRegisterDtoToAuth(GuestRegisterRequestDto dto);

    MailSenderGuestModel authToMailSenderGuestModel(Auth auth);
}
