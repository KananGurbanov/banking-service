package az.edu.turing.bankingservice.controller;

import az.edu.turing.bankingservice.model.dto.RestResponse;
import az.edu.turing.bankingservice.model.dto.response.RetrieveUserResponse;
import az.edu.turing.bankingservice.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestResponse<List<RetrieveUserResponse>>> getAllUsers() {
        List<RetrieveUserResponse> users = userService.getUsers();

        RestResponse<List<RetrieveUserResponse>> restResponse = RestResponse.<List<RetrieveUserResponse>>builder()
                .data(users)
                .status("SUCCESS")
                .build();

        return ResponseEntity.ok(restResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    @Hidden
    public String post() {
        return "POST:: admin controller";
    }


    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    @Hidden
    public String put() {
        return "PUT:: admin controller";
    }


    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    public String delete() {
        return "DELETE:: admin controller";
    }



}