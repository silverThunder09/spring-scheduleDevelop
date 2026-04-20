package com.scheduledevelop.user.service;

import com.scheduledevelop.user.dto.UserCreateRequestDto;
import com.scheduledevelop.user.dto.UserResponseDto;
import com.scheduledevelop.user.dto.UserUpdateRequestDto;
import com.scheduledevelop.user.entity.User;
import com.scheduledevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto create(UserCreateRequestDto request) {
        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());

        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new IllegalStateException("비밀번호는 8글자 이상이어야 합니다.");
        }

        User saved = userRepository.save(user);

        return new UserResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getOne(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 대한 유저가 없습니다. id = " + userId));

        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto update(Long userId, UserUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 대한 유저가 없습니다. id = " + userId));

        // 더티 체킹
        user.updateUser(request.getUsername(), request.getEmail());

        return new UserResponseDto(user);
    }

    @Transactional
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("해당 id에 대한 유저가 없습니다. id = " + userId));

        userRepository.delete(user);
    }
}
