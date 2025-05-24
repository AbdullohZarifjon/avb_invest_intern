package com.example.userservice.service;

import com.example.userservice.dto.CompanyResponseDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserGetAllDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.exps.RecordAlreadyException;
import com.example.userservice.exps.RecordNotFoundException;
import com.example.userservice.repo.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public UserServiceImpl(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public User createUser(UserCreateDto userCreateDto) {
        Optional<User> userByPhoneNumber = userRepository.getUserByPhoneNumber(userCreateDto.getPhoneNumber());
        if (userByPhoneNumber.isPresent()) {
            throw new RecordAlreadyException("User already exists");
        }
        User user = User.builder()
                .firstName(userCreateDto.getFirstName())
                .lastName(userCreateDto.getLastName())
                .phoneNumber(userCreateDto.getPhoneNumber())
                .companyId(userCreateDto.getCompanyId())
                .build();

        return userRepository.save(user);
    }

    @Override
    public UserGetAllDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        return UserGetAllDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .company(getCompanyById(user.getCompanyId()))
                .build();
    }


    @Override
    public User updateUser(Integer id, UserCreateDto userCreateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setCompanyId(userCreateDto.getCompanyId());
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RecordAlreadyException("This is phone number already exists");
        }
    }

    @Override
    public void deleteUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public void deleteUsersByCompanyId(Integer companyId) {
        userRepository.deleteUsersByCompanyId(companyId);
    }

    @Override
    public List<UserGetAllDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> UserGetAllDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .company(getCompanyById(user.getCompanyId()))
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUserByCompanyId(Integer id) {
        List<User> users = userRepository.getUsersByCompanyId(id);
        return users.stream().map(user -> UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build()).toList();

    }


    private CompanyResponseDto getCompanyById(Integer id) {
        String url = "http://company-service/api/companies/user/" + id;
        return restTemplate.getForObject(url, CompanyResponseDto.class);
    }

}
