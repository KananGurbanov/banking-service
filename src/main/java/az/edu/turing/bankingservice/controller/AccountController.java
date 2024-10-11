package az.edu.turing.bankingservice.controller;

import az.edu.turing.bankingservice.auth.AuthorizationHelperService;
import az.edu.turing.bankingservice.model.dto.response.RetrieveAccountResponse;
import az.edu.turing.bankingservice.model.dto.request.CreateAccountRequest;
import az.edu.turing.bankingservice.model.dto.RestResponse;
import az.edu.turing.bankingservice.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Controller API", description = "account controller")
public class AccountController {
    private final AccountService accountService;
    private final AuthorizationHelperService authorizationHelperService;

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestHeader("Authorization") String auth, @RequestBody @Valid CreateAccountRequest request) {
        Long userId = authorizationHelperService.getUserId(auth);
        log.info("Creating account for user {}", userId);
        accountService.createAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
        Long userId = authorizationHelperService.getUserId(auth);
        log.info("Deleted account for user  {} ", userId);
        accountService.deleteAccount(userId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<RetrieveAccountResponse>>> getAccounts(@RequestHeader("Authorization") String auth) {
        Long userId = authorizationHelperService.getUserId(auth);
        List<RetrieveAccountResponse> accounts = accountService.getAccounts(userId);
        log.info("Retrieved account for user  {} ", userId);
        RestResponse<List<RetrieveAccountResponse>> restResponse = RestResponse.<List<RetrieveAccountResponse>>builder()
                .data(accounts)
                .status("SUCCESS")
                .build();

        return ResponseEntity.ok(restResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<RetrieveAccountResponse>> getAccount(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
        Long userId = authorizationHelperService.getUserId(auth);
        RetrieveAccountResponse account = accountService.getAccount(userId, id);
        log.info("Retrieved account for user  {} ", userId);
        RestResponse<RetrieveAccountResponse> restResponse = RestResponse.<RetrieveAccountResponse>builder()
                .data(account)
                .status("SUCCESS")
                .build();

        return ResponseEntity.ok(restResponse);
    }

//    @PutMapping
//    public ResponseEntity<RestResponse<AccountEntity>> getAccountByIban(@RequestHeader("Authorization") String auth, @RequestParam String iban) {
//        Long userId = authorizationHelperService.getUserId(auth);
//        AccountEntity accountEntityByIban = accountService.getAccountEntityByIban(iban);
//        log.info("Retrieved account for user  {} ", userId);
//        RestResponse<AccountEntity> restResponse = RestResponse.<AccountEntity>builder()
//                .data(accountEntityByIban)
//                .status("SUCCESS")
//                .build();
//
//        return ResponseEntity.ok(restResponse);
//    }
}
