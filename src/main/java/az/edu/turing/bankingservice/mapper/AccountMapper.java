package az.edu.turing.bankingservice.mapper;

import az.edu.turing.bankingservice.dao.entity.AccountEntity;
import az.edu.turing.bankingservice.model.dto.response.RetrieveAccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    RetrieveAccountResponse mapToDto(AccountEntity accountEntity);

    Set<RetrieveAccountResponse> mapToDtoSet(Set<AccountEntity> accountEntities);
}
