package az.edu.turing.bankingservice.auth;

import az.edu.turing.bankingservice.dao.entity.UserEntity;
import az.edu.turing.bankingservice.dao.repository.UserRepository;
import az.edu.turing.bankingservice.exceptions.BadRequestException;
import az.edu.turing.bankingservice.exceptions.NotFoundException;
import az.edu.turing.bankingservice.model.dto.request.LoginUserRequest;
import az.edu.turing.bankingservice.model.dto.request.RegisterUserRequest;
import az.edu.turing.bankingservice.model.dto.response.LoginUserResponse;
import az.edu.turing.bankingservice.model.enums.Role;
import az.edu.turing.bankingservice.token.Token;
import az.edu.turing.bankingservice.token.TokenRepository;
import az.edu.turing.bankingservice.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static az.edu.turing.bankingservice.model.enums.Error.ERR_02;
import static az.edu.turing.bankingservice.model.enums.Error.ERR_03;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;


    public void register(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmail(registerUserRequest.email())) {
            throw new BadRequestException(ERR_02.getErrorCode(), ERR_02.getErrorDescription());
        }

        var user = UserEntity.builder()
                .name(registerUserRequest.name())
                .surname(registerUserRequest.surname())
                .email(registerUserRequest.email())
                .password(passwordEncoder.encode(registerUserRequest.password()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        log.info("Registered user with id: {}", savedUser.getId());
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {

        UserEntity user = userRepository.findByEmail(loginUserRequest.email())
                .orElseThrow(() -> new NotFoundException(ERR_03.getErrorCode(), ERR_03.getErrorDescription()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        loginUserRequest.password()
                )
        );

        var jwtToken = jwtService.generateToken(user.getId().toString());
        var refreshToken = jwtService.generateRefreshToken(user.getId().toString());
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return LoginUserResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUserId(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user.getId())) {
                var accessToken = jwtService.generateToken(user.getId().toString());
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = LoginUserResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
