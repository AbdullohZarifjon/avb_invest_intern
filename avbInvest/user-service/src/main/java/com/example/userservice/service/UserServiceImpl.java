package com.example.userservice.service;

import com.example.userservice.client.CompanyServiceClient;
import com.example.userservice.dto.CompanyResponseDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserGetAllComponentsDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.exps.RecordAlreadyException;
import com.example.userservice.exps.RecordNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyServiceClient companyServiceClient;

    public UserServiceImpl(UserRepository userRepository, CompanyServiceClient companyServiceClient) {
        this.userRepository = userRepository;
        this.companyServiceClient = companyServiceClient;
    }

    @Override
    public UserGetAllComponentsDto createUser(UserCreateDto userCreateDto) {
        checkUserPhoneNumberUniqueness(userCreateDto.getPhoneNumber());

        User user = User.builder()
                .firstName(userCreateDto.getFirstName())
                .lastName(userCreateDto.getLastName())
                .phoneNumber(userCreateDto.getPhoneNumber())
                .companyId(userCreateDto.getCompanyId())
                .build();

        userRepository.save(user);
        log.info("User saved: {}", user.getId());

        CompanyResponseDto companyResponseDto = companyServiceClient.getCompanyById(user.getCompanyId());
        return UserMapper.toDto(user, companyResponseDto);
    }

    @Override
    public UserGetAllComponentsDto getUserById(Integer userId) {
        User user = getUserOrThrow(userId);

        CompanyResponseDto companyResponseDto = companyServiceClient.getCompanyById(user.getCompanyId());
        log.info("Fetching user with ID: {}", userId);
        return UserMapper.toDto(user, companyResponseDto);
    }

    @Override
    public UserGetAllComponentsDto updateUser(Integer id, UserCreateDto userCreateDto) {
        User user = getUserOrThrow(id);

        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setCompanyId(userCreateDto.getCompanyId());

        try {
            userRepository.save(user);
            log.debug("User with ID {} updated successfully in DB:", id);
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate phone number {} for user ID {}", userCreateDto.getPhoneNumber(), id);
            throw new RecordAlreadyException("This phone number already exists");
        }

        CompanyResponseDto companyResponseDto = companyServiceClient.getCompanyById(user.getCompanyId());
        return UserMapper.toDto(user, companyResponseDto);
    }

    @Override
    public void deleteUserById(Integer userId) {
        User user = getUserOrThrow(userId);

        userRepository.delete(user);
        log.warn("User with id={} deleted", userId);
    }

    @Override
    public void deleteUsersByCompanyId(Integer companyId) {
        try {
            companyServiceClient.getCompanyById(companyId);
        } catch (RecordNotFoundException e) {
            log.warn("Company with ID {} not found. Skipping user deletion.", companyId);
            return;
        }

        int deletedCount = userRepository.deleteUsersByCompanyId(companyId);
        log.warn("Deleted {} users with companyId={}", deletedCount, companyId);
    }

    @Override
    public Page<UserGetAllComponentsDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        log.info("Fetched {} users from DB", users.getTotalElements());

        return users.map(user -> {
            CompanyResponseDto company = companyServiceClient.getCompanyById(user.getCompanyId());
            return UserMapper.toDto(user, company);
        });
    }

    @Override
    public List<UserResponseDto> getUserByCompanyId(Integer id) {
        List<User> users = userRepository.getUsersByCompanyId(id);
        return users.stream().map(UserMapper::toDto).toList();

    }

    private User getUserOrThrow(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User lookup failed. id={} not found in DB", id);
                    return new RecordNotFoundException("User not found with id: " + id);
                });
    }

    private void checkUserPhoneNumberUniqueness(String phoneNumber) {
        if (userRepository.getUserByPhoneNumber(phoneNumber).isPresent()) {
            log.warn("Attempted to create user with existing phoneNumber=*** (masked)");
            throw new RecordAlreadyException("User already exists");
        }
    }
}
