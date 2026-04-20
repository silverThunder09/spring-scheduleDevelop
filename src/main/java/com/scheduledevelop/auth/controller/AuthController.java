package com.scheduledevelop.auth.controller;

import com.scheduledevelop.auth.dto.LoginRequestDto;
import com.scheduledevelop.auth.dto.LoginResponseDto;
import com.scheduledevelop.auth.service.AuthService;
import com.scheduledevelop.common.SessionKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request, HttpServletRequest httpServletRequest) {
        LoginResponseDto response = authService.login(request);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SessionKey.LoginUserId, response.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);

        if (session == null || session.getAttribute(SessionKey.LoginUserId) == null) {
            throw new IllegalStateException("로그인이 필요한 요청입니다.");
        }

        // 세션 무효화
        session.invalidate();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
