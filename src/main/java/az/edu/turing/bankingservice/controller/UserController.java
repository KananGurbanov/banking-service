package az.edu.turing.bankingservice.controller;

import az.edu.turing.bankingservice.auth.AuthorizationHelperService;
import az.edu.turing.bankingservice.model.dto.RestResponse;
import az.edu.turing.bankingservice.model.dto.response.RetrieveUserResponse;
import az.edu.turing.bankingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthorizationHelperService authorizationHelperService;

    @GetMapping
    public ResponseEntity<RestResponse<RetrieveUserResponse>> getUserById(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = authorizationHelperService.getUserId(authHeader);

        RetrieveUserResponse user = userService.getUser(userId);

        RestResponse<RetrieveUserResponse> restResponse = RestResponse.<RetrieveUserResponse>builder()
                .data(user)
                .status("SUCCESS")
                .build();

        return ResponseEntity.ok(restResponse);
    }
}
