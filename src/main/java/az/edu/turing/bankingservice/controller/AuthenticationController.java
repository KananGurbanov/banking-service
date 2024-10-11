package az.edu.turing.bankingservice.controller;

import az.edu.turing.bankingservice.auth.AuthService;
import az.edu.turing.bankingservice.model.dto.request.LoginUserRequest;
import az.edu.turing.bankingservice.model.dto.request.RegisterUserRequest;
import az.edu.turing.bankingservice.model.dto.response.LoginUserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequest request) {
        service.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> authenticate(@RequestBody @Valid LoginUserRequest request) {
        return ResponseEntity.ok(service.login(request));
    }


//    @PostMapping("/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        logoutService.logout(request, response, authentication);
//    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }
}