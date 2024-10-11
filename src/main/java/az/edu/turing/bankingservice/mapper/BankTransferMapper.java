package az.edu.turing.bankingservice.mapper;

import az.edu.turing.bankingservice.dao.entity.TransactionEntity;
import az.edu.turing.bankingservice.model.dto.request.BankTransferRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankTransferMapper {
    BankTransferRequest mapToDto(TransactionEntity transactionEntity);
    Set<BankTransferRequest> mapToDtoSet(Set<TransactionEntity> bankTransferEntities);
}
