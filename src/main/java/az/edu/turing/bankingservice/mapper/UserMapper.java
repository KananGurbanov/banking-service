package az.edu.turing.bankingservice.mapper;

import az.edu.turing.bankingservice.dao.entity.UserEntity;
import az.edu.turing.bankingservice.model.dto.request.RegisterUserRequest;
import az.edu.turing.bankingservice.model.dto.response.RetrieveUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserEntity mapToEntity(RegisterUserRequest registerUserRequest);
    RetrieveUserResponse mapToDto(UserEntity userEntity);
    RetrieveUserResponse mapToDtoUsers(UserEntity userEntity);
}
