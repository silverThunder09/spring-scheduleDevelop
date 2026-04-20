package com.scheduledevelop.auth.service;

import com.scheduledevelop.auth.dto.LoginRequestDto;
import com.scheduledevelop.auth.dto.LoginResponseDto;
import com.scheduledevelop.common.SessionKey;
import com.scheduledevelop.user.entity.User;
import com.scheduledevelop.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalStateException("이메일은 필수입니다.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalStateException("비밀번호는 필수입니다.");
        }

        if (request.getPassword().length() < 8) {
            throw new IllegalStateException("비밀번호는 8글자 이상이어야 합니다.");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalStateException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return new LoginResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "로그인에 성공했습니다."
        );
    }

}
