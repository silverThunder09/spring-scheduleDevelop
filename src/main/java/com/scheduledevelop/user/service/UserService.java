package com.scheduledevelop.user.service;

import com.scheduledevelop.common.config.PasswordEncoder;
import com.scheduledevelop.common.exception.ApiException;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto create(UserCreateRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw ApiException.conflict("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encoderPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), request.getEmail(), encoderPassword);

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
                .orElseThrow(() -> ApiException.notFound("존재하지 않는 유저입니다."));

        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto update(Long userId,Long loginUserId, UserUpdateRequestDto request) {
        if (!userId.equals(loginUserId)) {
            throw ApiException.forbidden("본인 계정만 수정할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("존재하지 않는 유저입니다."));

        // 더티 체킹
        user.updateUser(request.getUsername(), request.getEmail());

        return new UserResponseDto(user);
    }

    @Transactional
    public void delete(Long userId, Long loginUserId) {
        if (!userId.equals(loginUserId)) {
            throw ApiException.forbidden("본인 계정만 삭제할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("존재하지 않는 유저입니다."));

        userRepository.delete(user);
    }
}
