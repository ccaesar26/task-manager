package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.dto.UserDto;
import com.is.lab.taskmanager.mapper.DtoMapper;
import com.is.lab.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(dtoMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
