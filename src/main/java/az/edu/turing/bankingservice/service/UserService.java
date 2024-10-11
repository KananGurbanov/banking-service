package az.edu.turing.bankingservice.service;

import az.edu.turing.bankingservice.model.dto.response.RetrieveUserResponse;

import java.util.List;

public interface UserService {
    List<RetrieveUserResponse> getUsers();
    RetrieveUserResponse getUser(Long id);
}
