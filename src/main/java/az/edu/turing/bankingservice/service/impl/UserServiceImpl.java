package az.edu.turing.bankingservice.service.impl;

import az.edu.turing.bankingservice.dao.repository.UserRepository;
import az.edu.turing.bankingservice.exceptions.NotFoundException;
import az.edu.turing.bankingservice.mapper.UserMapper;
import az.edu.turing.bankingservice.model.dto.response.RetrieveUserResponse;
import az.edu.turing.bankingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.edu.turing.bankingservice.model.enums.Error.ERR_03;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<RetrieveUserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::mapToDtoUsers).toList();
    }

    public RetrieveUserResponse getUser(Long userId) {
        return userMapper.mapToDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ERR_03.getErrorCode(), ERR_03.getErrorDescription())));
    }
}
