package az.edu.turing.bankingservice.controller;


import az.edu.turing.bankingservice.auth.AuthorizationHelperService;
import az.edu.turing.bankingservice.model.dto.request.BankTransferRequest;
import az.edu.turing.bankingservice.model.dto.request.TopUpRequest;
import az.edu.turing.bankingservice.service.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transfer Controller API", description = "transfer controller")
public class TransferController {
    private final TransferService transferService;
    private final AuthorizationHelperService authorizationHelperService;

    @PostMapping("/{id}/transfer")
    public ResponseEntity<Void> makeBankTransfer(@RequestHeader("Authorization") String auth,
                                                 @PathVariable("id") Long accountId,
                                                 @RequestBody @Valid BankTransferRequest bankTransferRequest) {
        Long userId = authorizationHelperService.getUserId(auth);
        transferService.makeBankTransfer(userId, accountId, bankTransferRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/topup")
    public ResponseEntity<Void> topUp(@RequestHeader("Authorization") String auth,
                                                 @PathVariable("id") Long accountId,
                                                 @RequestBody @Valid TopUpRequest topUpRequest) {
        Long userId = authorizationHelperService.getUserId(auth);
        transferService.topUp(userId, accountId, topUpRequest);
        return ResponseEntity.accepted().build();
    }
}

